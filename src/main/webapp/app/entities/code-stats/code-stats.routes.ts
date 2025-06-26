import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CodeStatsResolve from './route/code-stats-routing-resolve.service';

const codeStatsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/code-stats.component').then(m => m.CodeStatsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/code-stats-detail.component').then(m => m.CodeStatsDetailComponent),
    resolve: {
      codeStats: CodeStatsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/code-stats-update.component').then(m => m.CodeStatsUpdateComponent),
    resolve: {
      codeStats: CodeStatsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/code-stats-update.component').then(m => m.CodeStatsUpdateComponent),
    resolve: {
      codeStats: CodeStatsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default codeStatsRoute;
