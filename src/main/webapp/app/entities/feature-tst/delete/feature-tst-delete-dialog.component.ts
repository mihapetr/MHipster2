import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFeatureTst } from '../feature-tst.model';
import { FeatureTstService } from '../service/feature-tst.service';

@Component({
  templateUrl: './feature-tst-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FeatureTstDeleteDialogComponent {
  featureTst?: IFeatureTst;

  protected featureTstService = inject(FeatureTstService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.featureTstService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
