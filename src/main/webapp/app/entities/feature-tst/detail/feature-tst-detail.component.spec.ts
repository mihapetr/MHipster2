import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FeatureTstDetailComponent } from './feature-tst-detail.component';

describe('FeatureTst Management Detail Component', () => {
  let comp: FeatureTstDetailComponent;
  let fixture: ComponentFixture<FeatureTstDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeatureTstDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./feature-tst-detail.component').then(m => m.FeatureTstDetailComponent),
              resolve: { featureTst: () => of({ id: 14938 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FeatureTstDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeatureTstDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load featureTst on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FeatureTstDetailComponent);

      // THEN
      expect(instance.featureTst()).toEqual(expect.objectContaining({ id: 14938 }));
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
