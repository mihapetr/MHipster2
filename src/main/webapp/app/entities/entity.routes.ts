import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'mdls',
    data: { pageTitle: 'MDLS' },
    loadChildren: () => import('./mdls/mdls.routes'),
  },
  {
    path: 'project',
    data: { pageTitle: 'Projects' },
    loadChildren: () => import('./project/project.routes'),
  },
  {
    path: 'feature',
    data: { pageTitle: 'Features' },
    loadChildren: () => import('./feature/feature.routes'),
  },
  {
    path: 'feature-tst',
    data: { pageTitle: 'FeatureTsts' },
    loadChildren: () => import('./feature-tst/feature-tst.routes'),
  },
  {
    path: 'overview',
    data: { pageTitle: 'Overviews' },
    loadChildren: () => import('./overview/overview.routes'),
  },
  {
    path: 'code-stats',
    data: { pageTitle: 'CodeStats' },
    loadChildren: () => import('./code-stats/code-stats.routes'),
  },
  {
    path: 'test-report',
    data: { pageTitle: 'TestReports' },
    loadChildren: () => import('./test-report/test-report.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
