import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IFeatureTst } from '../feature-tst.model';
import { CodeStatsDetailComponent } from '../../code-stats/detail/code-stats-detail.component';
import { ICodeStats } from '../../code-stats/code-stats.model';
import { ITestReport } from '../../test-report/test-report.model';

@Component({
  selector: 'jhi-feature-tst-detail',
  templateUrl: './feature-tst-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, CodeStatsDetailComponent],
})
export class FeatureTstDetailComponent {
  featureTst = input<IFeatureTst | null>(null);

  protected readonly parent = parent;
  previousState(): void {
    window.history.back();
  }

  public getWholeNumbers(): ITestReport | undefined {
    return this.featureTst()?.testReports?.find(tr => tr.runtimeRetention === false);
  }

  public getHitNumbers(): ITestReport | undefined {
    const original = this.featureTst()?.testReports?.find(tr => tr.runtimeRetention === false);
    const testReport: ITestReport | undefined = original ? { ...original } : undefined;
    if (testReport?.missedLines && testReport.lines) testReport.lines = testReport.lines - testReport.missedLines;
    if (testReport?.missedMethods && testReport.methods) testReport.methods = testReport.methods - testReport.missedMethods;
    if (testReport?.classes && testReport.missedClasses) testReport.classes = testReport.classes - testReport.missedClasses;
    if (testReport?.instructions && testReport.missedInstructions)
      testReport.instructions = testReport.instructions - testReport.missedInstructions;
    if (testReport?.branches && testReport.missedBranches) testReport.branches = testReport.branches - testReport.missedBranches;
    return testReport;
  }
}
