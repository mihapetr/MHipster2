import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMDLS } from '../mdls.model';

@Component({
  selector: 'jhi-mdls-detail',
  templateUrl: './mdls-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MDLSDetailComponent {
  mDLS = input<IMDLS | null>(null);

  previousState(): void {
    window.history.back();
  }
}
