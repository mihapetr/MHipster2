import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IFeatureTst } from '../feature-tst.model';

@Component({
  selector: 'jhi-feature-tst-detail',
  templateUrl: './feature-tst-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class FeatureTstDetailComponent {
  featureTst = input<IFeatureTst | null>(null);

  previousState(): void {
    window.history.back();
  }
}
