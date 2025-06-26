import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICodeStats } from 'app/entities/code-stats/code-stats.model';
import { CodeStatsService } from 'app/entities/code-stats/service/code-stats.service';
import { IFeature } from 'app/entities/feature/feature.model';
import { FeatureService } from 'app/entities/feature/service/feature.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { FeatureTstService } from '../service/feature-tst.service';
import { IFeatureTst } from '../feature-tst.model';
import { FeatureTstFormGroup, FeatureTstFormService } from './feature-tst-form.service';

@Component({
  selector: 'jhi-feature-tst-update',
  templateUrl: './feature-tst-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeatureTstUpdateComponent implements OnInit {
  isSaving = false;
  featureTst: IFeatureTst | null = null;

  parentsCollection: ICodeStats[] = [];
  featuresSharedCollection: IFeature[] = [];
  projectsSharedCollection: IProject[] = [];

  protected featureTstService = inject(FeatureTstService);
  protected featureTstFormService = inject(FeatureTstFormService);
  protected codeStatsService = inject(CodeStatsService);
  protected featureService = inject(FeatureService);
  protected projectService = inject(ProjectService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeatureTstFormGroup = this.featureTstFormService.createFeatureTstFormGroup();

  compareCodeStats = (o1: ICodeStats | null, o2: ICodeStats | null): boolean => this.codeStatsService.compareCodeStats(o1, o2);

  compareFeature = (o1: IFeature | null, o2: IFeature | null): boolean => this.featureService.compareFeature(o1, o2);

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ featureTst }) => {
      this.featureTst = featureTst;
      if (featureTst) {
        this.updateForm(featureTst);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const featureTst = this.featureTstFormService.getFeatureTst(this.editForm);
    if (featureTst.id !== null) {
      this.subscribeToSaveResponse(this.featureTstService.update(featureTst));
    } else {
      this.subscribeToSaveResponse(this.featureTstService.create(featureTst));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeatureTst>>): void {
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

  protected updateForm(featureTst: IFeatureTst): void {
    this.featureTst = featureTst;
    this.featureTstFormService.resetForm(this.editForm, featureTst);

    this.parentsCollection = this.codeStatsService.addCodeStatsToCollectionIfMissing<ICodeStats>(this.parentsCollection, featureTst.parent);
    this.featuresSharedCollection = this.featureService.addFeatureToCollectionIfMissing<IFeature>(
      this.featuresSharedCollection,
      ...(featureTst.features ?? []),
    );
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      featureTst.project,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.codeStatsService
      .query({ filter: 'featuretst-is-null' })
      .pipe(map((res: HttpResponse<ICodeStats[]>) => res.body ?? []))
      .pipe(
        map((codeStats: ICodeStats[]) =>
          this.codeStatsService.addCodeStatsToCollectionIfMissing<ICodeStats>(codeStats, this.featureTst?.parent),
        ),
      )
      .subscribe((codeStats: ICodeStats[]) => (this.parentsCollection = codeStats));

    this.featureService
      .query()
      .pipe(map((res: HttpResponse<IFeature[]>) => res.body ?? []))
      .pipe(
        map((features: IFeature[]) =>
          this.featureService.addFeatureToCollectionIfMissing<IFeature>(features, ...(this.featureTst?.features ?? [])),
        ),
      )
      .subscribe((features: IFeature[]) => (this.featuresSharedCollection = features));

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.featureTst?.project)),
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }
}
