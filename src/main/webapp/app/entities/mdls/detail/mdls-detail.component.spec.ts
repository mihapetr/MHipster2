import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MDLSDetailComponent } from './mdls-detail.component';

describe('MDLS Management Detail Component', () => {
  let comp: MDLSDetailComponent;
  let fixture: ComponentFixture<MDLSDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MDLSDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./mdls-detail.component').then(m => m.MDLSDetailComponent),
              resolve: { mDLS: () => of({ id: 15572 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MDLSDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MDLSDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load mDLS on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MDLSDetailComponent);

      // THEN
      expect(instance.mDLS()).toEqual(expect.objectContaining({ id: 15572 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
