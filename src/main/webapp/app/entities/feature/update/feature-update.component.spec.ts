import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IFeatureTst } from 'app/entities/feature-tst/feature-tst.model';
import { FeatureTstService } from 'app/entities/feature-tst/service/feature-tst.service';
import { IFeature } from '../feature.model';
import { FeatureService } from '../service/feature.service';
import { FeatureFormService } from './feature-form.service';

import { FeatureUpdateComponent } from './feature-update.component';

describe('Feature Management Update Component', () => {
  let comp: FeatureUpdateComponent;
  let fixture: ComponentFixture<FeatureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let featureFormService: FeatureFormService;
  let featureService: FeatureService;
  let userService: UserService;
  let projectService: ProjectService;
  let featureTstService: FeatureTstService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeatureUpdateComponent],
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
      .overrideTemplate(FeatureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeatureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    featureFormService = TestBed.inject(FeatureFormService);
    featureService = TestBed.inject(FeatureService);
    userService = TestBed.inject(UserService);
    projectService = TestBed.inject(ProjectService);
    featureTstService = TestBed.inject(FeatureTstService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const feature: IFeature = { id: 19372 };
      const user: IUser = { id: 3944 };
      feature.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feature });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Project query and add missing value', () => {
      const feature: IFeature = { id: 19372 };
      const projects: IProject[] = [{ id: 10300 }];
      feature.projects = projects;

      const projectCollection: IProject[] = [{ id: 10300 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [...projects];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feature });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining),
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FeatureTst query and add missing value', () => {
      const feature: IFeature = { id: 19372 };
      const featureTsts: IFeatureTst[] = [{ id: 14938 }];
      feature.featureTsts = featureTsts;

      const featureTstCollection: IFeatureTst[] = [{ id: 14938 }];
      jest.spyOn(featureTstService, 'query').mockReturnValue(of(new HttpResponse({ body: featureTstCollection })));
      const additionalFeatureTsts = [...featureTsts];
      const expectedCollection: IFeatureTst[] = [...additionalFeatureTsts, ...featureTstCollection];
      jest.spyOn(featureTstService, 'addFeatureTstToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feature });
      comp.ngOnInit();

      expect(featureTstService.query).toHaveBeenCalled();
      expect(featureTstService.addFeatureTstToCollectionIfMissing).toHaveBeenCalledWith(
        featureTstCollection,
        ...additionalFeatureTsts.map(expect.objectContaining),
      );
      expect(comp.featureTstsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feature: IFeature = { id: 19372 };
      const user: IUser = { id: 3944 };
      feature.user = user;
      const project: IProject = { id: 10300 };
      feature.projects = [project];
      const featureTst: IFeatureTst = { id: 14938 };
      feature.featureTsts = [featureTst];

      activatedRoute.data = of({ feature });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.projectsSharedCollection).toContainEqual(project);
      expect(comp.featureTstsSharedCollection).toContainEqual(featureTst);
      expect(comp.feature).toEqual(feature);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeature>>();
      const feature = { id: 25842 };
      jest.spyOn(featureFormService, 'getFeature').mockReturnValue(feature);
      jest.spyOn(featureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feature }));
      saveSubject.complete();

      // THEN
      expect(featureFormService.getFeature).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(featureService.update).toHaveBeenCalledWith(expect.objectContaining(feature));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeature>>();
      const feature = { id: 25842 };
      jest.spyOn(featureFormService, 'getFeature').mockReturnValue({ id: null });
      jest.spyOn(featureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feature: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feature }));
      saveSubject.complete();

      // THEN
      expect(featureFormService.getFeature).toHaveBeenCalled();
      expect(featureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeature>>();
      const feature = { id: 25842 };
      jest.spyOn(featureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(featureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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
