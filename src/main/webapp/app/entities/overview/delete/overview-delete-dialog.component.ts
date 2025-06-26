import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOverview } from '../overview.model';
import { OverviewService } from '../service/overview.service';

@Component({
  templateUrl: './overview-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OverviewDeleteDialogComponent {
  overview?: IOverview;

  protected overviewService = inject(OverviewService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.overviewService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
