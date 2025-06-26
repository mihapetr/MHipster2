import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICodeStats } from 'app/entities/code-stats/code-stats.model';
import { CodeStatsService } from 'app/entities/code-stats/service/code-stats.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IOverview } from '../overview.model';
import { OverviewService } from '../service/overview.service';
import { OverviewFormService } from './overview-form.service';

import { OverviewUpdateComponent } from './overview-update.component';

describe('Overview Management Update Component', () => {
  let comp: OverviewUpdateComponent;
  let fixture: ComponentFixture<OverviewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let overviewFormService: OverviewFormService;
  let overviewService: OverviewService;
  let codeStatsService: CodeStatsService;
  let userService: UserService;
  let projectService: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OverviewUpdateComponent],
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
      .overrideTemplate(OverviewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OverviewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    overviewFormService = TestBed.inject(OverviewFormService);
    overviewService = TestBed.inject(OverviewService);
    codeStatsService = TestBed.inject(CodeStatsService);
    userService = TestBed.inject(UserService);
    projectService = TestBed.inject(ProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call parent query and add missing value', () => {
      const overview: IOverview = { id: 2729 };
      const parent: ICodeStats = { id: 16198 };
      overview.parent = parent;

      const parentCollection: ICodeStats[] = [{ id: 16198 }];
      jest.spyOn(codeStatsService, 'query').mockReturnValue(of(new HttpResponse({ body: parentCollection })));
      const expectedCollection: ICodeStats[] = [parent, ...parentCollection];
      jest.spyOn(codeStatsService, 'addCodeStatsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ overview });
      comp.ngOnInit();

      expect(codeStatsService.query).toHaveBeenCalled();
      expect(codeStatsService.addCodeStatsToCollectionIfMissing).toHaveBeenCalledWith(parentCollection, parent);
      expect(comp.parentsCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const overview: IOverview = { id: 2729 };
      const user: IUser = { id: 3944 };
      overview.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ overview });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Project query and add missing value', () => {
      const overview: IOverview = { id: 2729 };
      const projects: IProject[] = [{ id: 10300 }];
      overview.projects = projects;

      const projectCollection: IProject[] = [{ id: 10300 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [...projects];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ overview });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining),
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const overview: IOverview = { id: 2729 };
      const parent: ICodeStats = { id: 16198 };
      overview.parent = parent;
      const user: IUser = { id: 3944 };
      overview.user = user;
      const project: IProject = { id: 10300 };
      overview.projects = [project];

      activatedRoute.data = of({ overview });
      comp.ngOnInit();

      expect(comp.parentsCollection).toContainEqual(parent);
      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.projectsSharedCollection).toContainEqual(project);
      expect(comp.overview).toEqual(overview);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOverview>>();
      const overview = { id: 26694 };
      jest.spyOn(overviewFormService, 'getOverview').mockReturnValue(overview);
      jest.spyOn(overviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ overview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: overview }));
      saveSubject.complete();

      // THEN
      expect(overviewFormService.getOverview).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(overviewService.update).toHaveBeenCalledWith(expect.objectContaining(overview));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOverview>>();
      const overview = { id: 26694 };
      jest.spyOn(overviewFormService, 'getOverview').mockReturnValue({ id: null });
      jest.spyOn(overviewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ overview: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: overview }));
      saveSubject.complete();

      // THEN
      expect(overviewFormService.getOverview).toHaveBeenCalled();
      expect(overviewService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOverview>>();
      const overview = { id: 26694 };
      jest.spyOn(overviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ overview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(overviewService.update).toHaveBeenCalled();
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
  });
});
