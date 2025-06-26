import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MDLSResolve from './route/mdls-routing-resolve.service';

const mDLSRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/mdls.component').then(m => m.MDLSComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/mdls-detail.component').then(m => m.MDLSDetailComponent),
    resolve: {
      mDLS: MDLSResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/mdls-update.component').then(m => m.MDLSUpdateComponent),
    resolve: {
      mDLS: MDLSResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/mdls-update.component').then(m => m.MDLSUpdateComponent),
    resolve: {
      mDLS: MDLSResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mDLSRoute;
