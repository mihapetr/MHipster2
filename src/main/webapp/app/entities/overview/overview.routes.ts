import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OverviewResolve from './route/overview-routing-resolve.service';

const overviewRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/overview.component').then(m => m.OverviewComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/overview-detail.component').then(m => m.OverviewDetailComponent),
    resolve: {
      overview: OverviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/overview-update.component').then(m => m.OverviewUpdateComponent),
    resolve: {
      overview: OverviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/overview-update.component').then(m => m.OverviewUpdateComponent),
    resolve: {
      overview: OverviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default overviewRoute;
