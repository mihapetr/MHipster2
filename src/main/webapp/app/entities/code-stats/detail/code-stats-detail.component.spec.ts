import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CodeStatsDetailComponent } from './code-stats-detail.component';

describe('CodeStats Management Detail Component', () => {
  let comp: CodeStatsDetailComponent;
  let fixture: ComponentFixture<CodeStatsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CodeStatsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./code-stats-detail.component').then(m => m.CodeStatsDetailComponent),
              resolve: { codeStats: () => of({ id: 16198 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CodeStatsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CodeStatsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load codeStats on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CodeStatsDetailComponent);

      // THEN
      expect(instance.codeStats()).toEqual(expect.objectContaining({ id: 16198 }));
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
