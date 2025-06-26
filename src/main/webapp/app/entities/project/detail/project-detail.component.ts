import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IProject } from '../project.model';

@Component({
  selector: 'jhi-project-detail',
  templateUrl: './project-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ProjectDetailComponent {
  project = input<IProject | null>(null);

  previousState(): void {
    window.history.back();
  }
}
