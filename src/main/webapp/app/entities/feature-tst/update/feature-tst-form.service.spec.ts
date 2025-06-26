import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../feature-tst.test-samples';

import { FeatureTstFormService } from './feature-tst-form.service';

describe('FeatureTst Form Service', () => {
  let service: FeatureTstFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeatureTstFormService);
  });

  describe('Service methods', () => {
    describe('createFeatureTstFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeatureTstFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            parent: expect.any(Object),
            features: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });

      it('passing IFeatureTst should create a new form with FormGroup', () => {
        const formGroup = service.createFeatureTstFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            parent: expect.any(Object),
            features: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });
    });

    describe('getFeatureTst', () => {
      it('should return NewFeatureTst for default FeatureTst initial value', () => {
        const formGroup = service.createFeatureTstFormGroup(sampleWithNewData);

        const featureTst = service.getFeatureTst(formGroup) as any;

        expect(featureTst).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeatureTst for empty FeatureTst initial value', () => {
        const formGroup = service.createFeatureTstFormGroup();

        const featureTst = service.getFeatureTst(formGroup) as any;

        expect(featureTst).toMatchObject({});
      });

      it('should return IFeatureTst', () => {
        const formGroup = service.createFeatureTstFormGroup(sampleWithRequiredData);

        const featureTst = service.getFeatureTst(formGroup) as any;

        expect(featureTst).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeatureTst should not enable id FormControl', () => {
        const formGroup = service.createFeatureTstFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeatureTst should disable id FormControl', () => {
        const formGroup = service.createFeatureTstFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
