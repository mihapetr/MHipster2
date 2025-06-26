import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFeatureTst } from 'app/entities/feature-tst/feature-tst.model';
import { FeatureTstService } from 'app/entities/feature-tst/service/feature-tst.service';
import { ITestReport } from '../test-report.model';
import { TestReportService } from '../service/test-report.service';
import { TestReportFormGroup, TestReportFormService } from './test-report-form.service';

@Component({
  selector: 'jhi-test-report-update',
  templateUrl: './test-report-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestReportUpdateComponent implements OnInit {
  isSaving = false;
  testReport: ITestReport | null = null;

  featureTstsSharedCollection: IFeatureTst[] = [];

  protected testReportService = inject(TestReportService);
  protected testReportFormService = inject(TestReportFormService);
  protected featureTstService = inject(FeatureTstService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TestReportFormGroup = this.testReportFormService.createTestReportFormGroup();

  compareFeatureTst = (o1: IFeatureTst | null, o2: IFeatureTst | null): boolean => this.featureTstService.compareFeatureTst(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testReport }) => {
      this.testReport = testReport;
      if (testReport) {
        this.updateForm(testReport);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testReport = this.testReportFormService.getTestReport(this.editForm);
    if (testReport.id !== null) {
      this.subscribeToSaveResponse(this.testReportService.update(testReport));
    } else {
      this.subscribeToSaveResponse(this.testReportService.create(testReport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestReport>>): void {
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

  protected updateForm(testReport: ITestReport): void {
    this.testReport = testReport;
    this.testReportFormService.resetForm(this.editForm, testReport);

    this.featureTstsSharedCollection = this.featureTstService.addFeatureTstToCollectionIfMissing<IFeatureTst>(
      this.featureTstsSharedCollection,
      testReport.featureTst,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.featureTstService
      .query()
      .pipe(map((res: HttpResponse<IFeatureTst[]>) => res.body ?? []))
      .pipe(
        map((featureTsts: IFeatureTst[]) =>
          this.featureTstService.addFeatureTstToCollectionIfMissing<IFeatureTst>(featureTsts, this.testReport?.featureTst),
        ),
      )
      .subscribe((featureTsts: IFeatureTst[]) => (this.featureTstsSharedCollection = featureTsts));
  }
}
