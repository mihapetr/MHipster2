import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../overview.test-samples';

import { OverviewFormService } from './overview-form.service';

describe('Overview Form Service', () => {
  let service: OverviewFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OverviewFormService);
  });

  describe('Service methods', () => {
    describe('createOverviewFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOverviewFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateGenerated: expect.any(Object),
            parent: expect.any(Object),
            user: expect.any(Object),
            projects: expect.any(Object),
          }),
        );
      });

      it('passing IOverview should create a new form with FormGroup', () => {
        const formGroup = service.createOverviewFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateGenerated: expect.any(Object),
            parent: expect.any(Object),
            user: expect.any(Object),
            projects: expect.any(Object),
          }),
        );
      });
    });

    describe('getOverview', () => {
      it('should return NewOverview for default Overview initial value', () => {
        const formGroup = service.createOverviewFormGroup(sampleWithNewData);

        const overview = service.getOverview(formGroup) as any;

        expect(overview).toMatchObject(sampleWithNewData);
      });

      it('should return NewOverview for empty Overview initial value', () => {
        const formGroup = service.createOverviewFormGroup();

        const overview = service.getOverview(formGroup) as any;

        expect(overview).toMatchObject({});
      });

      it('should return IOverview', () => {
        const formGroup = service.createOverviewFormGroup(sampleWithRequiredData);

        const overview = service.getOverview(formGroup) as any;

        expect(overview).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOverview should not enable id FormControl', () => {
        const formGroup = service.createOverviewFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOverview should disable id FormControl', () => {
        const formGroup = service.createOverviewFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
