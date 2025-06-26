import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../code-stats.test-samples';

import { CodeStatsFormService } from './code-stats-form.service';

describe('CodeStats Form Service', () => {
  let service: CodeStatsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CodeStatsFormService);
  });

  describe('Service methods', () => {
    describe('createCodeStatsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCodeStatsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            instructions: expect.any(Object),
            branches: expect.any(Object),
            lines: expect.any(Object),
            methods: expect.any(Object),
            classes: expect.any(Object),
          }),
        );
      });

      it('passing ICodeStats should create a new form with FormGroup', () => {
        const formGroup = service.createCodeStatsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            instructions: expect.any(Object),
            branches: expect.any(Object),
            lines: expect.any(Object),
            methods: expect.any(Object),
            classes: expect.any(Object),
          }),
        );
      });
    });

    describe('getCodeStats', () => {
      it('should return NewCodeStats for default CodeStats initial value', () => {
        const formGroup = service.createCodeStatsFormGroup(sampleWithNewData);

        const codeStats = service.getCodeStats(formGroup) as any;

        expect(codeStats).toMatchObject(sampleWithNewData);
      });

      it('should return NewCodeStats for empty CodeStats initial value', () => {
        const formGroup = service.createCodeStatsFormGroup();

        const codeStats = service.getCodeStats(formGroup) as any;

        expect(codeStats).toMatchObject({});
      });

      it('should return ICodeStats', () => {
        const formGroup = service.createCodeStatsFormGroup(sampleWithRequiredData);

        const codeStats = service.getCodeStats(formGroup) as any;

        expect(codeStats).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICodeStats should not enable id FormControl', () => {
        const formGroup = service.createCodeStatsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCodeStats should disable id FormControl', () => {
        const formGroup = service.createCodeStatsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
