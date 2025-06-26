import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IFeatureTst } from 'app/entities/feature-tst/feature-tst.model';
import { FeatureTstService } from 'app/entities/feature-tst/service/feature-tst.service';
import { FeatureService } from '../service/feature.service';
import { IFeature } from '../feature.model';
import { FeatureFormGroup, FeatureFormService } from './feature-form.service';

@Component({
  selector: 'jhi-feature-update',
  templateUrl: './feature-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeatureUpdateComponent implements OnInit {
  isSaving = false;
  feature: IFeature | null = null;

  usersSharedCollection: IUser[] = [];
  projectsSharedCollection: IProject[] = [];
  featureTstsSharedCollection: IFeatureTst[] = [];

  protected featureService = inject(FeatureService);
  protected featureFormService = inject(FeatureFormService);
  protected userService = inject(UserService);
  protected projectService = inject(ProjectService);
  protected featureTstService = inject(FeatureTstService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeatureFormGroup = this.featureFormService.createFeatureFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  compareFeatureTst = (o1: IFeatureTst | null, o2: IFeatureTst | null): boolean => this.featureTstService.compareFeatureTst(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feature }) => {
      this.feature = feature;
      if (feature) {
        this.updateForm(feature);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feature = this.featureFormService.getFeature(this.editForm);
    if (feature.id !== null) {
      this.subscribeToSaveResponse(this.featureService.update(feature));
    } else {
      this.subscribeToSaveResponse(this.featureService.create(feature));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeature>>): void {
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

  protected updateForm(feature: IFeature): void {
    this.feature = feature;
    this.featureFormService.resetForm(this.editForm, feature);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, feature.user);
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      ...(feature.projects ?? []),
    );
    this.featureTstsSharedCollection = this.featureTstService.addFeatureTstToCollectionIfMissing<IFeatureTst>(
      this.featureTstsSharedCollection,
      ...(feature.featureTsts ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.feature?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) =>
          this.projectService.addProjectToCollectionIfMissing<IProject>(projects, ...(this.feature?.projects ?? [])),
        ),
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));

    this.featureTstService
      .query()
      .pipe(map((res: HttpResponse<IFeatureTst[]>) => res.body ?? []))
      .pipe(
        map((featureTsts: IFeatureTst[]) =>
          this.featureTstService.addFeatureTstToCollectionIfMissing<IFeatureTst>(featureTsts, ...(this.feature?.featureTsts ?? [])),
        ),
      )
      .subscribe((featureTsts: IFeatureTst[]) => (this.featureTstsSharedCollection = featureTsts));
  }
}
