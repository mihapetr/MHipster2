import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TestReportDetailComponent } from './test-report-detail.component';

describe('TestReport Management Detail Component', () => {
  let comp: TestReportDetailComponent;
  let fixture: ComponentFixture<TestReportDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestReportDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./test-report-detail.component').then(m => m.TestReportDetailComponent),
              resolve: { testReport: () => of({ id: 29997 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TestReportDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestReportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load testReport on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TestReportDetailComponent);

      // THEN
      expect(instance.testReport()).toEqual(expect.objectContaining({ id: 29997 }));
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
