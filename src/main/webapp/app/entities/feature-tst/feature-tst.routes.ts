import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FeatureTstResolve from './route/feature-tst-routing-resolve.service';

const featureTstRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/feature-tst.component').then(m => m.FeatureTstComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/feature-tst-detail.component').then(m => m.FeatureTstDetailComponent),
    resolve: {
      featureTst: FeatureTstResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/feature-tst-update.component').then(m => m.FeatureTstUpdateComponent),
    resolve: {
      featureTst: FeatureTstResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/feature-tst-update.component').then(m => m.FeatureTstUpdateComponent),
    resolve: {
      featureTst: FeatureTstResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default featureTstRoute;
