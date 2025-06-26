import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TestReportResolve from './route/test-report-routing-resolve.service';

const testReportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/test-report.component').then(m => m.TestReportComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/test-report-detail.component').then(m => m.TestReportDetailComponent),
    resolve: {
      testReport: TestReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/test-report-update.component').then(m => m.TestReportUpdateComponent),
    resolve: {
      testReport: TestReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/test-report-update.component').then(m => m.TestReportUpdateComponent),
    resolve: {
      testReport: TestReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default testReportRoute;
