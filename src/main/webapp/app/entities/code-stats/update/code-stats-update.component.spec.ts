import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CodeStatsService } from '../service/code-stats.service';
import { ICodeStats } from '../code-stats.model';
import { CodeStatsFormService } from './code-stats-form.service';

import { CodeStatsUpdateComponent } from './code-stats-update.component';

describe('CodeStats Management Update Component', () => {
  let comp: CodeStatsUpdateComponent;
  let fixture: ComponentFixture<CodeStatsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let codeStatsFormService: CodeStatsFormService;
  let codeStatsService: CodeStatsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CodeStatsUpdateComponent],
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
      .overrideTemplate(CodeStatsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodeStatsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codeStatsFormService = TestBed.inject(CodeStatsFormService);
    codeStatsService = TestBed.inject(CodeStatsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const codeStats: ICodeStats = { id: 3264 };

      activatedRoute.data = of({ codeStats });
      comp.ngOnInit();

      expect(comp.codeStats).toEqual(codeStats);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICodeStats>>();
      const codeStats = { id: 16198 };
      jest.spyOn(codeStatsFormService, 'getCodeStats').mockReturnValue(codeStats);
      jest.spyOn(codeStatsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeStats });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codeStats }));
      saveSubject.complete();

      // THEN
      expect(codeStatsFormService.getCodeStats).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(codeStatsService.update).toHaveBeenCalledWith(expect.objectContaining(codeStats));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICodeStats>>();
      const codeStats = { id: 16198 };
      jest.spyOn(codeStatsFormService, 'getCodeStats').mockReturnValue({ id: null });
      jest.spyOn(codeStatsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeStats: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codeStats }));
      saveSubject.complete();

      // THEN
      expect(codeStatsFormService.getCodeStats).toHaveBeenCalled();
      expect(codeStatsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICodeStats>>();
      const codeStats = { id: 16198 };
      jest.spyOn(codeStatsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codeStats });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codeStatsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
