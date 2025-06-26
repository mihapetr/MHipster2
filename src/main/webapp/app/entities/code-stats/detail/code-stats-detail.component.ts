import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICodeStats } from '../code-stats.model';

@Component({
  selector: 'jhi-code-stats-detail',
  templateUrl: './code-stats-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CodeStatsDetailComponent {
  codeStats = input<ICodeStats | null>(null);

  previousState(): void {
    window.history.back();
  }
}
