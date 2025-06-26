import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMDLS } from 'app/entities/mdls/mdls.model';
import { MDLSService } from 'app/entities/mdls/service/mdls.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IFeature } from 'app/entities/feature/feature.model';
import { FeatureService } from 'app/entities/feature/service/feature.service';
import { IOverview } from 'app/entities/overview/overview.model';
import { OverviewService } from 'app/entities/overview/service/overview.service';
import { IProject } from '../project.model';
import { ProjectService } from '../service/project.service';
import { ProjectFormService } from './project-form.service';

import { ProjectUpdateComponent } from './project-update.component';

describe('Project Management Update Component', () => {
  let comp: ProjectUpdateComponent;
  let fixture: ComponentFixture<ProjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectFormService: ProjectFormService;
  let projectService: ProjectService;
  let mDLSService: MDLSService;
  let userService: UserService;
  let featureService: FeatureService;
  let overviewService: OverviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProjectUpdateComponent],
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
      .overrideTemplate(ProjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectFormService = TestBed.inject(ProjectFormService);
    projectService = TestBed.inject(ProjectService);
    mDLSService = TestBed.inject(MDLSService);
    userService = TestBed.inject(UserService);
    featureService = TestBed.inject(FeatureService);
    overviewService = TestBed.inject(OverviewService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call mdls query and add missing value', () => {
      const project: IProject = { id: 3319 };
      const mdls: IMDLS = { id: 15572 };
      project.mdls = mdls;

      const mdlsCollection: IMDLS[] = [{ id: 15572 }];
      jest.spyOn(mDLSService, 'query').mockReturnValue(of(new HttpResponse({ body: mdlsCollection })));
      const expectedCollection: IMDLS[] = [mdls, ...mdlsCollection];
      jest.spyOn(mDLSService, 'addMDLSToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(mDLSService.query).toHaveBeenCalled();
      expect(mDLSService.addMDLSToCollectionIfMissing).toHaveBeenCalledWith(mdlsCollection, mdls);
      expect(comp.mdlsCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const project: IProject = { id: 3319 };
      const user: IUser = { id: 3944 };
      project.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Feature query and add missing value', () => {
      const project: IProject = { id: 3319 };
      const features: IFeature[] = [{ id: 25842 }];
      project.features = features;

      const featureCollection: IFeature[] = [{ id: 25842 }];
      jest.spyOn(featureService, 'query').mockReturnValue(of(new HttpResponse({ body: featureCollection })));
      const additionalFeatures = [...features];
      const expectedCollection: IFeature[] = [...additionalFeatures, ...featureCollection];
      jest.spyOn(featureService, 'addFeatureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(featureService.query).toHaveBeenCalled();
      expect(featureService.addFeatureToCollectionIfMissing).toHaveBeenCalledWith(
        featureCollection,
        ...additionalFeatures.map(expect.objectContaining),
      );
      expect(comp.featuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Overview query and add missing value', () => {
      const project: IProject = { id: 3319 };
      const overviews: IOverview[] = [{ id: 26694 }];
      project.overviews = overviews;

      const overviewCollection: IOverview[] = [{ id: 26694 }];
      jest.spyOn(overviewService, 'query').mockReturnValue(of(new HttpResponse({ body: overviewCollection })));
      const additionalOverviews = [...overviews];
      const expectedCollection: IOverview[] = [...additionalOverviews, ...overviewCollection];
      jest.spyOn(overviewService, 'addOverviewToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(overviewService.query).toHaveBeenCalled();
      expect(overviewService.addOverviewToCollectionIfMissing).toHaveBeenCalledWith(
        overviewCollection,
        ...additionalOverviews.map(expect.objectContaining),
      );
      expect(comp.overviewsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const project: IProject = { id: 3319 };
      const mdls: IMDLS = { id: 15572 };
      project.mdls = mdls;
      const user: IUser = { id: 3944 };
      project.user = user;
      const feature: IFeature = { id: 25842 };
      project.features = [feature];
      const overview: IOverview = { id: 26694 };
      project.overviews = [overview];

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(comp.mdlsCollection).toContainEqual(mdls);
      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.featuresSharedCollection).toContainEqual(feature);
      expect(comp.overviewsSharedCollection).toContainEqual(overview);
      expect(comp.project).toEqual(project);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 10300 };
      jest.spyOn(projectFormService, 'getProject').mockReturnValue(project);
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(projectFormService.getProject).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectService.update).toHaveBeenCalledWith(expect.objectContaining(project));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 10300 };
      jest.spyOn(projectFormService, 'getProject').mockReturnValue({ id: null });
      jest.spyOn(projectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(projectFormService.getProject).toHaveBeenCalled();
      expect(projectService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 10300 };
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMDLS', () => {
      it('Should forward to mDLSService', () => {
        const entity = { id: 15572 };
        const entity2 = { id: 18176 };
        jest.spyOn(mDLSService, 'compareMDLS');
        comp.compareMDLS(entity, entity2);
        expect(mDLSService.compareMDLS).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareOverview', () => {
      it('Should forward to overviewService', () => {
        const entity = { id: 26694 };
        const entity2 = { id: 2729 };
        jest.spyOn(overviewService, 'compareOverview');
        comp.compareOverview(entity, entity2);
        expect(overviewService.compareOverview).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
