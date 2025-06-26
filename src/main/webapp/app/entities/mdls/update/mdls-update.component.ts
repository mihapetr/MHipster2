import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { MDLSService } from '../service/mdls.service';
import { IMDLS } from '../mdls.model';
import { MDLSFormGroup, MDLSFormService } from './mdls-form.service';

@Component({
  selector: 'jhi-mdls-update',
  templateUrl: './mdls-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MDLSUpdateComponent implements OnInit {
  isSaving = false;
  mDLS: IMDLS | null = null;

  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected mDLSService = inject(MDLSService);
  protected mDLSFormService = inject(MDLSFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MDLSFormGroup = this.mDLSFormService.createMDLSFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mDLS }) => {
      this.mDLS = mDLS;
      if (mDLS) {
        this.updateForm(mDLS);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('mHipsterApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mDLS = this.mDLSFormService.getMDLS(this.editForm);
    if (mDLS.id !== null) {
      this.subscribeToSaveResponse(this.mDLSService.update(mDLS));
    } else {
      this.subscribeToSaveResponse(this.mDLSService.create(mDLS));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMDLS>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mDLS: IMDLS): void {
    this.mDLS = mDLS;
    this.mDLSFormService.resetForm(this.editForm, mDLS);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, mDLS.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.mDLS?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
