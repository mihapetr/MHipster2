import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IProject } from '../project.model';
import { IMDLS } from '../../mdls/mdls.model';
import { MDLSDetailComponent } from '../../mdls/detail/mdls-detail.component';
import { ProjectService } from '../service/project.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-project-detail',
  templateUrl: './project-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, MDLSDetailComponent],
})
export class ProjectDetailComponent {
  project = input<IProject | null>(null);

  protected projectService = inject(ProjectService);

  previousState(): void {
    window.history.back();
  }

  generateFiles(id: number): void {
    this.subscribeToGenerateResponse(this.projectService.generateFiles(id));
  }

  downloadFiles(id: number): void {
    this.subscribeToDownloadResponse(id);
  }

  protected subscribeToDownloadResponse(id: number): void {
    this.projectService.downloadFiles(id).subscribe({
      next(blob: Blob) {
        // Create a link element, trigger the download
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        // Optionally, get filename from the Content-Disposition header via an interceptor or pass it through service
        a.download = `project_${id}.zip`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
      },
      error() {
        // You could use your alert service here to show an error to the user
        // Example: this.alertService.error('Download failed!');
      },
    });
  }

  protected subscribeToGenerateResponse(result: Observable<HttpResponse<IProject>>): void {
    result.subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    // this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }
}
