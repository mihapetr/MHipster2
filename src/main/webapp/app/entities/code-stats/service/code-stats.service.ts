import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICodeStats, NewCodeStats } from '../code-stats.model';

export type PartialUpdateCodeStats = Partial<ICodeStats> & Pick<ICodeStats, 'id'>;

export type EntityResponseType = HttpResponse<ICodeStats>;
export type EntityArrayResponseType = HttpResponse<ICodeStats[]>;

@Injectable({ providedIn: 'root' })
export class CodeStatsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/code-stats');

  create(codeStats: NewCodeStats): Observable<EntityResponseType> {
    return this.http.post<ICodeStats>(this.resourceUrl, codeStats, { observe: 'response' });
  }

  update(codeStats: ICodeStats): Observable<EntityResponseType> {
    return this.http.put<ICodeStats>(`${this.resourceUrl}/${this.getCodeStatsIdentifier(codeStats)}`, codeStats, { observe: 'response' });
  }

  partialUpdate(codeStats: PartialUpdateCodeStats): Observable<EntityResponseType> {
    return this.http.patch<ICodeStats>(`${this.resourceUrl}/${this.getCodeStatsIdentifier(codeStats)}`, codeStats, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICodeStats>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICodeStats[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCodeStatsIdentifier(codeStats: Pick<ICodeStats, 'id'>): number {
    return codeStats.id;
  }

  compareCodeStats(o1: Pick<ICodeStats, 'id'> | null, o2: Pick<ICodeStats, 'id'> | null): boolean {
    return o1 && o2 ? this.getCodeStatsIdentifier(o1) === this.getCodeStatsIdentifier(o2) : o1 === o2;
  }

  addCodeStatsToCollectionIfMissing<Type extends Pick<ICodeStats, 'id'>>(
    codeStatsCollection: Type[],
    ...codeStatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const codeStats: Type[] = codeStatsToCheck.filter(isPresent);
    if (codeStats.length > 0) {
      const codeStatsCollectionIdentifiers = codeStatsCollection.map(codeStatsItem => this.getCodeStatsIdentifier(codeStatsItem));
      const codeStatsToAdd = codeStats.filter(codeStatsItem => {
        const codeStatsIdentifier = this.getCodeStatsIdentifier(codeStatsItem);
        if (codeStatsCollectionIdentifiers.includes(codeStatsIdentifier)) {
          return false;
        }
        codeStatsCollectionIdentifiers.push(codeStatsIdentifier);
        return true;
      });
      return [...codeStatsToAdd, ...codeStatsCollection];
    }
    return codeStatsCollection;
  }
}
