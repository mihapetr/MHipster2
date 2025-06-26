import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IFeatureTst } from 'app/entities/feature-tst/feature-tst.model';
import { FeatureTstService } from 'app/entities/feature-tst/service/feature-tst.service';
import { TestReportService } from '../service/test-report.service';
import { ITestReport } from '../test-report.model';
import { TestReportFormService } from './test-report-form.service';

import { TestReportUpdateComponent } from './test-report-update.component';

describe('TestReport Management Update Component', () => {
  let comp: TestReportUpdateComponent;
  let fixture: ComponentFixture<TestReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testReportFormService: TestReportFormService;
  let testReportService: TestReportService;
  let featureTstService: FeatureTstService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TestReportUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TestReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testReportFormService = TestBed.inject(TestReportFormService);
    testReportService = TestBed.inject(TestReportService);
    featureTstService = TestBed.inject(FeatureTstService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FeatureTst query and add missing value', () => {
      const testReport: ITestReport = { id: 6462 };
      const featureTst: IFeatureTst = { id: 14938 };
      testReport.featureTst = featureTst;

      const featureTstCollection: IFeatureTst[] = [{ id: 14938 }];
      jest.spyOn(featureTstService, 'query').mockReturnValue(of(new HttpResponse({ body: featureTstCollection })));
      const additionalFeatureTsts = [featureTst];
      const expectedCollection: IFeatureTst[] = [...additionalFeatureTsts, ...featureTstCollection];
      jest.spyOn(featureTstService, 'addFeatureTstToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testReport });
      comp.ngOnInit();

      expect(featureTstService.query).toHaveBeenCalled();
      expect(featureTstService.addFeatureTstToCollectionIfMissing).toHaveBeenCalledWith(
        featureTstCollection,
        ...additionalFeatureTsts.map(expect.objectContaining),
      );
      expect(comp.featureTstsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const testReport: ITestReport = { id: 6462 };
      const featureTst: IFeatureTst = { id: 14938 };
      testReport.featureTst = featureTst;

      activatedRoute.data = of({ testReport });
      comp.ngOnInit();

      expect(comp.featureTstsSharedCollection).toContainEqual(featureTst);
      expect(comp.testReport).toEqual(testReport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestReport>>();
      const testReport = { id: 29997 };
      jest.spyOn(testReportFormService, 'getTestReport').mockReturnValue(testReport);
      jest.spyOn(testReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testReport }));
      saveSubject.complete();

      // THEN
      expect(testReportFormService.getTestReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testReportService.update).toHaveBeenCalledWith(expect.objectContaining(testReport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestReport>>();
      const testReport = { id: 29997 };
      jest.spyOn(testReportFormService, 'getTestReport').mockReturnValue({ id: null });
      jest.spyOn(testReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testReport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testReport }));
      saveSubject.complete();

      // THEN
      expect(testReportFormService.getTestReport).toHaveBeenCalled();
      expect(testReportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestReport>>();
      const testReport = { id: 29997 };
      jest.spyOn(testReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testReportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFeatureTst', () => {
      it('Should forward to featureTstService', () => {
        const entity = { id: 14938 };
        const entity2 = { id: 24103 };
        jest.spyOn(featureTstService, 'compareFeatureTst');
        comp.compareFeatureTst(entity, entity2);
        expect(featureTstService.compareFeatureTst).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
