import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IOverview } from '../overview.model';

@Component({
  selector: 'jhi-overview-detail',
  templateUrl: './overview-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class OverviewDetailComponent {
  overview = input<IOverview | null>(null);

  previousState(): void {
    window.history.back();
  }
}
