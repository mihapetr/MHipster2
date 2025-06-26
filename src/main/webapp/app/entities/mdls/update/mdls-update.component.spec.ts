import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { MDLSService } from '../service/mdls.service';
import { IMDLS } from '../mdls.model';
import { MDLSFormService } from './mdls-form.service';

import { MDLSUpdateComponent } from './mdls-update.component';

describe('MDLS Management Update Component', () => {
  let comp: MDLSUpdateComponent;
  let fixture: ComponentFixture<MDLSUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mDLSFormService: MDLSFormService;
  let mDLSService: MDLSService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MDLSUpdateComponent],
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
      .overrideTemplate(MDLSUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MDLSUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mDLSFormService = TestBed.inject(MDLSFormService);
    mDLSService = TestBed.inject(MDLSService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const mDLS: IMDLS = { id: 18176 };
      const user: IUser = { id: 3944 };
      mDLS.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mDLS });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mDLS: IMDLS = { id: 18176 };
      const user: IUser = { id: 3944 };
      mDLS.user = user;

      activatedRoute.data = of({ mDLS });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.mDLS).toEqual(mDLS);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMDLS>>();
      const mDLS = { id: 15572 };
      jest.spyOn(mDLSFormService, 'getMDLS').mockReturnValue(mDLS);
      jest.spyOn(mDLSService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mDLS });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mDLS }));
      saveSubject.complete();

      // THEN
      expect(mDLSFormService.getMDLS).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mDLSService.update).toHaveBeenCalledWith(expect.objectContaining(mDLS));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMDLS>>();
      const mDLS = { id: 15572 };
      jest.spyOn(mDLSFormService, 'getMDLS').mockReturnValue({ id: null });
      jest.spyOn(mDLSService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mDLS: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mDLS }));
      saveSubject.complete();

      // THEN
      expect(mDLSFormService.getMDLS).toHaveBeenCalled();
      expect(mDLSService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMDLS>>();
      const mDLS = { id: 15572 };
      jest.spyOn(mDLSService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mDLS });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mDLSService.update).toHaveBeenCalled();
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
  });
});
