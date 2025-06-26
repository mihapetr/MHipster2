import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMDLS } from '../mdls.model';
import { MDLSService } from '../service/mdls.service';

@Component({
  templateUrl: './mdls-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MDLSDeleteDialogComponent {
  mDLS?: IMDLS;

  protected mDLSService = inject(MDLSService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mDLSService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
