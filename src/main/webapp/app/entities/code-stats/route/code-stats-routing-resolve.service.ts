import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICodeStats } from '../code-stats.model';
import { CodeStatsService } from '../service/code-stats.service';

const codeStatsResolve = (route: ActivatedRouteSnapshot): Observable<null | ICodeStats> => {
  const id = route.params.id;
  if (id) {
    return inject(CodeStatsService)
      .find(id)
      .pipe(
        mergeMap((codeStats: HttpResponse<ICodeStats>) => {
          if (codeStats.body) {
            return of(codeStats.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default codeStatsResolve;
