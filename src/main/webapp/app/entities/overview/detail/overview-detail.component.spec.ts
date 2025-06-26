import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { OverviewDetailComponent } from './overview-detail.component';

describe('Overview Management Detail Component', () => {
  let comp: OverviewDetailComponent;
  let fixture: ComponentFixture<OverviewDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverviewDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./overview-detail.component').then(m => m.OverviewDetailComponent),
              resolve: { overview: () => of({ id: 26694 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OverviewDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OverviewDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load overview on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OverviewDetailComponent);

      // THEN
      expect(instance.overview()).toEqual(expect.objectContaining({ id: 26694 }));
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
