import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMDLS } from 'app/entities/mdls/mdls.model';
import { MDLSService } from 'app/entities/mdls/service/mdls.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IFeature } from 'app/entities/feature/feature.model';
import { FeatureService } from 'app/entities/feature/service/feature.service';
import { IOverview } from 'app/entities/overview/overview.model';
import { OverviewService } from 'app/entities/overview/service/overview.service';
import { ProjectService } from '../service/project.service';
import { IProject } from '../project.model';
import { ProjectFormGroup, ProjectFormService } from './project-form.service';
import { FeatureComponent } from '../../feature/list/feature.component';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProjectUpdateComponent implements OnInit {
  isSaving = false;
  project: IProject | null = null;

  mdlsCollection: IMDLS[] = [];
  usersSharedCollection: IUser[] = [];
  featuresSharedCollection: IFeature[] = [];
  overviewsSharedCollection: IOverview[] = [];

  protected projectService = inject(ProjectService);
  protected projectFormService = inject(ProjectFormService);
  protected mDLSService = inject(MDLSService);
  protected userService = inject(UserService);
  protected featureService = inject(FeatureService);
  protected overviewService = inject(OverviewService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProjectFormGroup = this.projectFormService.createProjectFormGroup();

  compareMDLS = (o1: IMDLS | null, o2: IMDLS | null): boolean => this.mDLSService.compareMDLS(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareFeature = (o1: IFeature | null, o2: IFeature | null): boolean => this.featureService.compareFeature(o1, o2);

  compareOverview = (o1: IOverview | null, o2: IOverview | null): boolean => this.overviewService.compareOverview(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
      if (project) {
        this.updateForm(project);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const project = this.projectFormService.getProject(this.editForm);
    if (project.id !== null) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  public summary(content: string | null | undefined): string {
    const entityNames: string[] = [];
    const lines = content!.split('\n');

    const entityRegex = /^\s*Scenario:\s+([\w\s]+)/;

    for (const line of lines) {
      const match = line.match(entityRegex);
      if (match) {
        entityNames.push(match[1]);
      }
    }

    return entityNames.join('\n');
  }

  public mdlsSummary(content: string | null | undefined): string {
    const entityNames: string[] = [];
    const lines = content!.split('\n');

    const entityRegex = /^\s*entity\s+(\w+)/;

    for (const line of lines) {
      const match = line.match(entityRegex);
      if (match) {
        entityNames.push(match[1]);
      }
    }

    return entityNames.join(', ');
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>): void {
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

  protected updateForm(project: IProject): void {
    this.project = project;
    this.projectFormService.resetForm(this.editForm, project);

    this.mdlsCollection = this.mDLSService.addMDLSToCollectionIfMissing<IMDLS>(this.mdlsCollection, project.mdls);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, project.user);
    this.featuresSharedCollection = this.featureService.addFeatureToCollectionIfMissing<IFeature>(
      this.featuresSharedCollection,
      ...(project.features ?? []),
    );
    this.overviewsSharedCollection = this.overviewService.addOverviewToCollectionIfMissing<IOverview>(
      this.overviewsSharedCollection,
      ...(project.overviews ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mDLSService
      .query({ filter: 'project-is-null' })
      .pipe(map((res: HttpResponse<IMDLS[]>) => res.body ?? []))
      .pipe(map((mDLS: IMDLS[]) => this.mDLSService.addMDLSToCollectionIfMissing<IMDLS>(mDLS, this.project?.mdls)))
      .subscribe((mDLS: IMDLS[]) => (this.mdlsCollection = mDLS));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.project?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.featureService
      .query({ filter: 'current-user' })
      .pipe(map((res: HttpResponse<IFeature[]>) => res.body ?? []))
      .pipe(
        map((features: IFeature[]) =>
          this.featureService.addFeatureToCollectionIfMissing<IFeature>(features, ...(this.project?.features ?? [])),
        ),
      )
      .subscribe((features: IFeature[]) => (this.featuresSharedCollection = features));

    // this.overviewService
    //   .query()
    //   .pipe(map((res: HttpResponse<IOverview[]>) => res.body ?? []))
    //   .pipe(
    //     map((overviews: IOverview[]) =>
    //       this.overviewService.addOverviewToCollectionIfMissing<IOverview>(overviews, ...(this.project?.overviews ?? [])),
    //     ),
    //   )
    //   .subscribe((overviews: IOverview[]) => (this.overviewsSharedCollection = overviews));
  }
}
