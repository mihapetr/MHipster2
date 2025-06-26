import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOverview } from '../overview.model';
import { OverviewService } from '../service/overview.service';

const overviewResolve = (route: ActivatedRouteSnapshot): Observable<null | IOverview> => {
  const id = route.params.id;
  if (id) {
    return inject(OverviewService)
      .find(id)
      .pipe(
        mergeMap((overview: HttpResponse<IOverview>) => {
          if (overview.body) {
            return of(overview.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default overviewResolve;
