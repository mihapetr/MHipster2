import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICodeStats } from 'app/entities/code-stats/code-stats.model';
import { CodeStatsService } from 'app/entities/code-stats/service/code-stats.service';
import { IFeature } from 'app/entities/feature/feature.model';
import { FeatureService } from 'app/entities/feature/service/feature.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IFeatureTst } from '../feature-tst.model';
import { FeatureTstService } from '../service/feature-tst.service';
import { FeatureTstFormService } from './feature-tst-form.service';

import { FeatureTstUpdateComponent } from './feature-tst-update.component';

describe('FeatureTst Management Update Component', () => {
  let comp: FeatureTstUpdateComponent;
  let fixture: ComponentFixture<FeatureTstUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let featureTstFormService: FeatureTstFormService;
  let featureTstService: FeatureTstService;
  let codeStatsService: CodeStatsService;
  let featureService: FeatureService;
  let projectService: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeatureTstUpdateComponent],
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
      .overrideTemplate(FeatureTstUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeatureTstUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    featureTstFormService = TestBed.inject(FeatureTstFormService);
    featureTstService = TestBed.inject(FeatureTstService);
    codeStatsService = TestBed.inject(CodeStatsService);
    featureService = TestBed.inject(FeatureService);
    projectService = TestBed.inject(ProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call parent query and add missing value', () => {
      const featureTst: IFeatureTst = { id: 24103 };
      const parent: ICodeStats = { id: 16198 };
      featureTst.parent = parent;

      const parentCollection: ICodeStats[] = [{ id: 16198 }];
      jest.spyOn(codeStatsService, 'query').mockReturnValue(of(new HttpResponse({ body: parentCollection })));
      const expectedCollection: ICodeStats[] = [parent, ...parentCollection];
      jest.spyOn(codeStatsService, 'addCodeStatsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ featureTst });
      comp.ngOnInit();

      expect(codeStatsService.query).toHaveBeenCalled();
      expect(codeStatsService.addCodeStatsToCollectionIfMissing).toHaveBeenCalledWith(parentCollection, parent);
      expect(comp.parentsCollection).toEqual(expectedCollection);
    });

    it('Should call Feature query and add missing value', () => {
      const featureTst: IFeatureTst = { id: 24103 };
      const features: IFeature[] = [{ id: 25842 }];
      featureTst.features = features;

      const featureCollection: IFeature[] = [{ id: 25842 }];
      jest.spyOn(featureService, 'query').mockReturnValue(of(new HttpResponse({ body: featureCollection })));
      const additionalFeatures = [...features];
      const expectedCollection: IFeature[] = [...additionalFeatures, ...featureCollection];
      jest.spyOn(featureService, 'addFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ featureTst });
      comp.ngOnInit();

      expect(featureService.query).toHaveBeenCalled();
      expect(featureService.addFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        featureCollection,
        ...additionalFeatures.map(expect.objectContaining),
      );
      expect(comp.featuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Project query and add missing value', () => {
      const featureTst: IFeatureTst = { id: 24103 };
      const project: IProject = { id: 10300 };
      featureTst.project = project;

      const projectCollection: IProject[] = [{ id: 10300 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [project];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ featureTst });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining),
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const featureTst: IFeatureTst = { id: 24103 };
      const parent: ICodeStats = { id: 16198 };
      featureTst.parent = parent;
      const feature: IFeature = { id: 25842 };
      featureTst.features = [feature];
      const project: IProject = { id: 10300 };
      featureTst.project = project;

      activatedRoute.data = of({ featureTst });
      comp.ngOnInit();

      expect(comp.parentsCollection).toContainEqual(parent);
      expect(comp.featuresSharedCollection).toContainEqual(feature);
      expect(comp.projectsSharedCollection).toContainEqual(project);
      expect(comp.featureTst).toEqual(featureTst);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeatureTst>>();
      const featureTst = { id: 14938 };
      jest.spyOn(featureTstFormService, 'getFeatureTst').mockReturnValue(featureTst);
      jest.spyOn(featureTstService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ featureTst });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: featureTst }));
      saveSubject.complete();

      // THEN
      expect(featureTstFormService.getFeatureTst).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(featureTstService.update).toHaveBeenCalledWith(expect.objectContaining(featureTst));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeatureTst>>();
      const featureTst = { id: 14938 };
      jest.spyOn(featureTstFormService, 'getFeatureTst').mockReturnValue({ id: null });
      jest.spyOn(featureTstService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ featureTst: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: featureTst }));
      saveSubject.complete();

      // THEN
      expect(featureTstFormService.getFeatureTst).toHaveBeenCalled();
      expect(featureTstService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeatureTst>>();
      const featureTst = { id: 14938 };
      jest.spyOn(featureTstService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ featureTst });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(featureTstService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCodeStats', () => {
      it('Should forward to codeStatsService', () => {
        const entity = { id: 16198 };
        const entity2 = { id: 3264 };
        jest.spyOn(codeStatsService, 'compareCodeStats');
        comp.compareCodeStats(entity, entity2);
        expect(codeStatsService.compareCodeStats).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFeature', () => {
      it('Should forward to featureService', () => {
        const entity = { id: 25842 };
        const entity2 = { id: 19372 };
        jest.spyOn(featureService, 'compareFeature');
        comp.compareFeature(entity, entity2);
        expect(featureService.compareFeature).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProject', () => {
      it('Should forward to projectService', () => {
        const entity = { id: 10300 };
        const entity2 = { id: 3319 };
        jest.spyOn(projectService, 'compareProject');
        comp.compareProject(entity, entity2);
        expect(projectService.compareProject).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
