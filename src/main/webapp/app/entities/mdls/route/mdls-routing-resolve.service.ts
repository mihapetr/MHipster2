import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMDLS } from '../mdls.model';
import { MDLSService } from '../service/mdls.service';

const mDLSResolve = (route: ActivatedRouteSnapshot): Observable<null | IMDLS> => {
  const id = route.params.id;
  if (id) {
    return inject(MDLSService)
      .find(id)
      .pipe(
        mergeMap((mDLS: HttpResponse<IMDLS>) => {
          if (mDLS.body) {
            return of(mDLS.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default mDLSResolve;
