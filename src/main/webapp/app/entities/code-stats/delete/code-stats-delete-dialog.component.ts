import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICodeStats } from '../code-stats.model';
import { CodeStatsService } from '../service/code-stats.service';

@Component({
  templateUrl: './code-stats-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CodeStatsDeleteDialogComponent {
  codeStats?: ICodeStats;

  protected codeStatsService = inject(CodeStatsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.codeStatsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
