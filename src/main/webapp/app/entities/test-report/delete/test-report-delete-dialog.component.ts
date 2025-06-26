import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITestReport } from '../test-report.model';
import { TestReportService } from '../service/test-report.service';

@Component({
  templateUrl: './test-report-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TestReportDeleteDialogComponent {
  testReport?: ITestReport;

  protected testReportService = inject(TestReportService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testReportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
