import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../mdls.test-samples';

import { MDLSFormService } from './mdls-form.service';

describe('MDLS Form Service', () => {
  let service: MDLSFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MDLSFormService);
  });

  describe('Service methods', () => {
    describe('createMDLSFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMDLSFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            baseConfig: expect.any(Object),
            content: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IMDLS should create a new form with FormGroup', () => {
        const formGroup = service.createMDLSFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            baseConfig: expect.any(Object),
            content: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getMDLS', () => {
      it('should return NewMDLS for default MDLS initial value', () => {
        const formGroup = service.createMDLSFormGroup(sampleWithNewData);

        const mDLS = service.getMDLS(formGroup) as any;

        expect(mDLS).toMatchObject(sampleWithNewData);
      });

      it('should return NewMDLS for empty MDLS initial value', () => {
        const formGroup = service.createMDLSFormGroup();

        const mDLS = service.getMDLS(formGroup) as any;

        expect(mDLS).toMatchObject({});
      });

      it('should return IMDLS', () => {
        const formGroup = service.createMDLSFormGroup(sampleWithRequiredData);

        const mDLS = service.getMDLS(formGroup) as any;

        expect(mDLS).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMDLS should not enable id FormControl', () => {
        const formGroup = service.createMDLSFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMDLS should disable id FormControl', () => {
        const formGroup = service.createMDLSFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
