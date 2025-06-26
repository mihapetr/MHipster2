import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITestReport, NewTestReport } from '../test-report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestReport for edit and NewTestReportFormGroupInput for create.
 */
type TestReportFormGroupInput = ITestReport | PartialWithRequiredKeyOf<NewTestReport>;

type TestReportFormDefaults = Pick<NewTestReport, 'id' | 'runtimeRetention'>;

type TestReportFormGroupContent = {
  id: FormControl<ITestReport['id'] | NewTestReport['id']>;
  html: FormControl<ITestReport['html']>;
  runtimeRetention: FormControl<ITestReport['runtimeRetention']>;
  missedInstructions: FormControl<ITestReport['missedInstructions']>;
  instructions: FormControl<ITestReport['instructions']>;
  missedBranches: FormControl<ITestReport['missedBranches']>;
  branches: FormControl<ITestReport['branches']>;
  missedLines: FormControl<ITestReport['missedLines']>;
  lines: FormControl<ITestReport['lines']>;
  missedMethods: FormControl<ITestReport['missedMethods']>;
  methods: FormControl<ITestReport['methods']>;
  missedClasses: FormControl<ITestReport['missedClasses']>;
  classes: FormControl<ITestReport['classes']>;
  featureTst: FormControl<ITestReport['featureTst']>;
};

export type TestReportFormGroup = FormGroup<TestReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestReportFormService {
  createTestReportFormGroup(testReport: TestReportFormGroupInput = { id: null }): TestReportFormGroup {
    const testReportRawValue = {
      ...this.getFormDefaults(),
      ...testReport,
    };
    return new FormGroup<TestReportFormGroupContent>({
      id: new FormControl(
        { value: testReportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      html: new FormControl(testReportRawValue.html),
      runtimeRetention: new FormControl(testReportRawValue.runtimeRetention),
      missedInstructions: new FormControl(testReportRawValue.missedInstructions),
      instructions: new FormControl(testReportRawValue.instructions),
      missedBranches: new FormControl(testReportRawValue.missedBranches),
      branches: new FormControl(testReportRawValue.branches),
      missedLines: new FormControl(testReportRawValue.missedLines),
      lines: new FormControl(testReportRawValue.lines),
      missedMethods: new FormControl(testReportRawValue.missedMethods),
      methods: new FormControl(testReportRawValue.methods),
      missedClasses: new FormControl(testReportRawValue.missedClasses),
      classes: new FormControl(testReportRawValue.classes),
      featureTst: new FormControl(testReportRawValue.featureTst, {
        validators: [Validators.required],
      }),
    });
  }

  getTestReport(form: TestReportFormGroup): ITestReport | NewTestReport {
    return form.getRawValue() as ITestReport | NewTestReport;
  }

  resetForm(form: TestReportFormGroup, testReport: TestReportFormGroupInput): void {
    const testReportRawValue = { ...this.getFormDefaults(), ...testReport };
    form.reset(
      {
        ...testReportRawValue,
        id: { value: testReportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TestReportFormDefaults {
    return {
      id: null,
      runtimeRetention: false,
    };
  }
}
