import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICodeStats } from '../code-stats.model';
import { CodeStatsService } from '../service/code-stats.service';
import { CodeStatsFormGroup, CodeStatsFormService } from './code-stats-form.service';

@Component({
  selector: 'jhi-code-stats-update',
  templateUrl: './code-stats-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CodeStatsUpdateComponent implements OnInit {
  isSaving = false;
  codeStats: ICodeStats | null = null;

  protected codeStatsService = inject(CodeStatsService);
  protected codeStatsFormService = inject(CodeStatsFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CodeStatsFormGroup = this.codeStatsFormService.createCodeStatsFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codeStats }) => {
      this.codeStats = codeStats;
      if (codeStats) {
        this.updateForm(codeStats);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const codeStats = this.codeStatsFormService.getCodeStats(this.editForm);
    if (codeStats.id !== null) {
      this.subscribeToSaveResponse(this.codeStatsService.update(codeStats));
    } else {
      this.subscribeToSaveResponse(this.codeStatsService.create(codeStats));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodeStats>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(codeStats: ICodeStats): void {
    this.codeStats = codeStats;
    this.codeStatsFormService.resetForm(this.editForm, codeStats);
  }
}
