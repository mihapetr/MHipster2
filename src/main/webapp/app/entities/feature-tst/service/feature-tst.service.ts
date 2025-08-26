import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeatureTst, NewFeatureTst } from '../feature-tst.model';

export type PartialUpdateFeatureTst = Partial<IFeatureTst> & Pick<IFeatureTst, 'id'>;

type RestOf<T extends IFeatureTst | NewFeatureTst> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestFeatureTst = RestOf<IFeatureTst>;

export type NewRestFeatureTst = RestOf<NewFeatureTst>;

export type PartialUpdateRestFeatureTst = RestOf<PartialUpdateFeatureTst>;

export type EntityResponseType = HttpResponse<IFeatureTst>;
export type EntityArrayResponseType = HttpResponse<IFeatureTst[]>;

@Injectable({ providedIn: 'root' })
export class FeatureTstService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feature-tsts');

  create(featureTst: NewFeatureTst): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(featureTst);
    return this.http
      .post<RestFeatureTst>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(featureTst: IFeatureTst): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(featureTst);
    return this.http
      .put<RestFeatureTst>(`${this.resourceUrl}/${this.getFeatureTstIdentifier(featureTst)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(featureTst: PartialUpdateFeatureTst): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(featureTst);
    return this.http
      .patch<RestFeatureTst>(`${this.resourceUrl}/${this.getFeatureTstIdentifier(featureTst)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFeatureTst>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFeatureTst[]>(`${this.resourceUrl}?filter=current-user`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeatureTstIdentifier(featureTst: Pick<IFeatureTst, 'id'>): number {
    return featureTst.id;
  }

  compareFeatureTst(o1: Pick<IFeatureTst, 'id'> | null, o2: Pick<IFeatureTst, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeatureTstIdentifier(o1) === this.getFeatureTstIdentifier(o2) : o1 === o2;
  }

  addFeatureTstToCollectionIfMissing<Type extends Pick<IFeatureTst, 'id'>>(
    featureTstCollection: Type[],
    ...featureTstsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const featureTsts: Type[] = featureTstsToCheck.filter(isPresent);
    if (featureTsts.length > 0) {
      const featureTstCollectionIdentifiers = featureTstCollection.map(featureTstItem => this.getFeatureTstIdentifier(featureTstItem));
      const featureTstsToAdd = featureTsts.filter(featureTstItem => {
        const featureTstIdentifier = this.getFeatureTstIdentifier(featureTstItem);
        if (featureTstCollectionIdentifiers.includes(featureTstIdentifier)) {
          return false;
        }
        featureTstCollectionIdentifiers.push(featureTstIdentifier);
        return true;
      });
      return [...featureTstsToAdd, ...featureTstCollection];
    }
    return featureTstCollection;
  }

  protected convertDateFromClient<T extends IFeatureTst | NewFeatureTst | PartialUpdateFeatureTst>(featureTst: T): RestOf<T> {
    return {
      ...featureTst,
      date: featureTst.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFeatureTst: RestFeatureTst): IFeatureTst {
    return {
      ...restFeatureTst,
      date: restFeatureTst.date ? dayjs(restFeatureTst.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFeatureTst>): HttpResponse<IFeatureTst> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFeatureTst[]>): HttpResponse<IFeatureTst[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
