import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICodeStats } from 'app/entities/code-stats/code-stats.model';
import { CodeStatsService } from 'app/entities/code-stats/service/code-stats.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { OverviewService } from '../service/overview.service';
import { IOverview } from '../overview.model';
import { OverviewFormGroup, OverviewFormService } from './overview-form.service';

@Component({
  selector: 'jhi-overview-update',
  templateUrl: './overview-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OverviewUpdateComponent implements OnInit {
  isSaving = false;
  overview: IOverview | null = null;

  parentsCollection: ICodeStats[] = [];
  usersSharedCollection: IUser[] = [];
  projectsSharedCollection: IProject[] = [];

  protected overviewService = inject(OverviewService);
  protected overviewFormService = inject(OverviewFormService);
  protected codeStatsService = inject(CodeStatsService);
  protected userService = inject(UserService);
  protected projectService = inject(ProjectService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OverviewFormGroup = this.overviewFormService.createOverviewFormGroup();

  compareCodeStats = (o1: ICodeStats | null, o2: ICodeStats | null): boolean => this.codeStatsService.compareCodeStats(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ overview }) => {
      this.overview = overview;
      if (overview) {
        this.updateForm(overview);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const overview = this.overviewFormService.getOverview(this.editForm);
    if (overview.id !== null) {
      this.subscribeToSaveResponse(this.overviewService.update(overview));
    } else {
      this.subscribeToSaveResponse(this.overviewService.create(overview));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOverview>>): void {
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

  protected updateForm(overview: IOverview): void {
    this.overview = overview;
    this.overviewFormService.resetForm(this.editForm, overview);

    this.parentsCollection = this.codeStatsService.addCodeStatsToCollectionIfMissing<ICodeStats>(this.parentsCollection, overview.parent);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, overview.user);
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      ...(overview.projects ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.codeStatsService
      .query({ filter: 'overview-is-null' })
      .pipe(map((res: HttpResponse<ICodeStats[]>) => res.body ?? []))
      .pipe(
        map((codeStats: ICodeStats[]) =>
          this.codeStatsService.addCodeStatsToCollectionIfMissing<ICodeStats>(codeStats, this.overview?.parent),
        ),
      )
      .subscribe((codeStats: ICodeStats[]) => (this.parentsCollection = codeStats));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.overview?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) =>
          this.projectService.addProjectToCollectionIfMissing<IProject>(projects, ...(this.overview?.projects ?? [])),
        ),
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }
}
