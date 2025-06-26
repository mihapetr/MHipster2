import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../test-report.test-samples';

import { TestReportFormService } from './test-report-form.service';

describe('TestReport Form Service', () => {
  let service: TestReportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestReportFormService);
  });

  describe('Service methods', () => {
    describe('createTestReportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTestReportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            html: expect.any(Object),
            runtimeRetention: expect.any(Object),
            missedInstructions: expect.any(Object),
            instructions: expect.any(Object),
            missedBranches: expect.any(Object),
            branches: expect.any(Object),
            missedLines: expect.any(Object),
            lines: expect.any(Object),
            missedMethods: expect.any(Object),
            methods: expect.any(Object),
            missedClasses: expect.any(Object),
            classes: expect.any(Object),
            featureTst: expect.any(Object),
          }),
        );
      });

      it('passing ITestReport should create a new form with FormGroup', () => {
        const formGroup = service.createTestReportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            html: expect.any(Object),
            runtimeRetention: expect.any(Object),
            missedInstructions: expect.any(Object),
            instructions: expect.any(Object),
            missedBranches: expect.any(Object),
            branches: expect.any(Object),
            missedLines: expect.any(Object),
            lines: expect.any(Object),
            missedMethods: expect.any(Object),
            methods: expect.any(Object),
            missedClasses: expect.any(Object),
            classes: expect.any(Object),
            featureTst: expect.any(Object),
          }),
        );
      });
    });

    describe('getTestReport', () => {
      it('should return NewTestReport for default TestReport initial value', () => {
        const formGroup = service.createTestReportFormGroup(sampleWithNewData);

        const testReport = service.getTestReport(formGroup) as any;

        expect(testReport).toMatchObject(sampleWithNewData);
      });

      it('should return NewTestReport for empty TestReport initial value', () => {
        const formGroup = service.createTestReportFormGroup();

        const testReport = service.getTestReport(formGroup) as any;

        expect(testReport).toMatchObject({});
      });

      it('should return ITestReport', () => {
        const formGroup = service.createTestReportFormGroup(sampleWithRequiredData);

        const testReport = service.getTestReport(formGroup) as any;

        expect(testReport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITestReport should not enable id FormControl', () => {
        const formGroup = service.createTestReportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTestReport should disable id FormControl', () => {
        const formGroup = service.createTestReportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
