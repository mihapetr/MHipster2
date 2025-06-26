import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeatureTst } from '../feature-tst.model';
import { FeatureTstService } from '../service/feature-tst.service';

const featureTstResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeatureTst> => {
  const id = route.params.id;
  if (id) {
    return inject(FeatureTstService)
      .find(id)
      .pipe(
        mergeMap((featureTst: HttpResponse<IFeatureTst>) => {
          if (featureTst.body) {
            return of(featureTst.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default featureTstResolve;
