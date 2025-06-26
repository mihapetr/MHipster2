import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITestReport } from '../test-report.model';
import { TestReportService } from '../service/test-report.service';

const testReportResolve = (route: ActivatedRouteSnapshot): Observable<null | ITestReport> => {
  const id = route.params.id;
  if (id) {
    return inject(TestReportService)
      .find(id)
      .pipe(
        mergeMap((testReport: HttpResponse<ITestReport>) => {
          if (testReport.body) {
            return of(testReport.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default testReportResolve;
