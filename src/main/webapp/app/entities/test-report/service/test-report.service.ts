import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestReport, NewTestReport } from '../test-report.model';

export type PartialUpdateTestReport = Partial<ITestReport> & Pick<ITestReport, 'id'>;

export type EntityResponseType = HttpResponse<ITestReport>;
export type EntityArrayResponseType = HttpResponse<ITestReport[]>;

@Injectable({ providedIn: 'root' })
export class TestReportService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/test-reports');

  create(testReport: NewTestReport): Observable<EntityResponseType> {
    return this.http.post<ITestReport>(this.resourceUrl, testReport, { observe: 'response' });
  }

  update(testReport: ITestReport): Observable<EntityResponseType> {
    return this.http.put<ITestReport>(`${this.resourceUrl}/${this.getTestReportIdentifier(testReport)}`, testReport, {
      observe: 'response',
    });
  }

  partialUpdate(testReport: PartialUpdateTestReport): Observable<EntityResponseType> {
    return this.http.patch<ITestReport>(`${this.resourceUrl}/${this.getTestReportIdentifier(testReport)}`, testReport, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTestReportIdentifier(testReport: Pick<ITestReport, 'id'>): number {
    return testReport.id;
  }

  compareTestReport(o1: Pick<ITestReport, 'id'> | null, o2: Pick<ITestReport, 'id'> | null): boolean {
    return o1 && o2 ? this.getTestReportIdentifier(o1) === this.getTestReportIdentifier(o2) : o1 === o2;
  }

  addTestReportToCollectionIfMissing<Type extends Pick<ITestReport, 'id'>>(
    testReportCollection: Type[],
    ...testReportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const testReports: Type[] = testReportsToCheck.filter(isPresent);
    if (testReports.length > 0) {
      const testReportCollectionIdentifiers = testReportCollection.map(testReportItem => this.getTestReportIdentifier(testReportItem));
      const testReportsToAdd = testReports.filter(testReportItem => {
        const testReportIdentifier = this.getTestReportIdentifier(testReportItem);
        if (testReportCollectionIdentifiers.includes(testReportIdentifier)) {
          return false;
        }
        testReportCollectionIdentifiers.push(testReportIdentifier);
        return true;
      });
      return [...testReportsToAdd, ...testReportCollection];
    }
    return testReportCollection;
  }
}
