import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITestReport } from '../test-report.model';

@Component({
  selector: 'jhi-test-report-detail',
  templateUrl: './test-report-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TestReportDetailComponent {
  testReport = input<ITestReport | null>(null);

  previousState(): void {
    window.history.back();
  }
}
