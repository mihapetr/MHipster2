import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMDLS, NewMDLS } from '../mdls.model';

export type PartialUpdateMDLS = Partial<IMDLS> & Pick<IMDLS, 'id'>;

export type EntityResponseType = HttpResponse<IMDLS>;
export type EntityArrayResponseType = HttpResponse<IMDLS[]>;

@Injectable({ providedIn: 'root' })
export class MDLSService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mdls');

  create(mDLS: NewMDLS): Observable<EntityResponseType> {
    return this.http.post<IMDLS>(this.resourceUrl, mDLS, { observe: 'response' });
  }

  update(mDLS: IMDLS): Observable<EntityResponseType> {
    return this.http.put<IMDLS>(`${this.resourceUrl}/${this.getMDLSIdentifier(mDLS)}`, mDLS, { observe: 'response' });
  }

  partialUpdate(mDLS: PartialUpdateMDLS): Observable<EntityResponseType> {
    return this.http.patch<IMDLS>(`${this.resourceUrl}/${this.getMDLSIdentifier(mDLS)}`, mDLS, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMDLS>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMDLS[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMDLSIdentifier(mDLS: Pick<IMDLS, 'id'>): number {
    return mDLS.id;
  }

  compareMDLS(o1: Pick<IMDLS, 'id'> | null, o2: Pick<IMDLS, 'id'> | null): boolean {
    return o1 && o2 ? this.getMDLSIdentifier(o1) === this.getMDLSIdentifier(o2) : o1 === o2;
  }

  addMDLSToCollectionIfMissing<Type extends Pick<IMDLS, 'id'>>(
    mDLSCollection: Type[],
    ...mDLSToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mDLS: Type[] = mDLSToCheck.filter(isPresent);
    if (mDLS.length > 0) {
      const mDLSCollectionIdentifiers = mDLSCollection.map(mDLSItem => this.getMDLSIdentifier(mDLSItem));
      const mDLSToAdd = mDLS.filter(mDLSItem => {
        const mDLSIdentifier = this.getMDLSIdentifier(mDLSItem);
        if (mDLSCollectionIdentifiers.includes(mDLSIdentifier)) {
          return false;
        }
        mDLSCollectionIdentifiers.push(mDLSIdentifier);
        return true;
      });
      return [...mDLSToAdd, ...mDLSCollection];
    }
    return mDLSCollection;
  }
}
