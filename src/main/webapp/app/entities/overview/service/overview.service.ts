import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOverview, NewOverview } from '../overview.model';

export type PartialUpdateOverview = Partial<IOverview> & Pick<IOverview, 'id'>;

type RestOf<T extends IOverview | NewOverview> = Omit<T, 'dateGenerated'> & {
  dateGenerated?: string | null;
};

export type RestOverview = RestOf<IOverview>;

export type NewRestOverview = RestOf<NewOverview>;

export type PartialUpdateRestOverview = RestOf<PartialUpdateOverview>;

export type EntityResponseType = HttpResponse<IOverview>;
export type EntityArrayResponseType = HttpResponse<IOverview[]>;

@Injectable({ providedIn: 'root' })
export class OverviewService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/overviews');

  create(overview: NewOverview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(overview);
    return this.http
      .post<RestOverview>(this.resourceUrl + '/generate', copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(overview: IOverview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(overview);
    return this.http
      .put<RestOverview>(`${this.resourceUrl}/${this.getOverviewIdentifier(overview)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(overview: PartialUpdateOverview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(overview);
    return this.http
      .patch<RestOverview>(`${this.resourceUrl}/${this.getOverviewIdentifier(overview)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOverview>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOverview[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOverviewIdentifier(overview: Pick<IOverview, 'id'>): number {
    return overview.id;
  }

  compareOverview(o1: Pick<IOverview, 'id'> | null, o2: Pick<IOverview, 'id'> | null): boolean {
    return o1 && o2 ? this.getOverviewIdentifier(o1) === this.getOverviewIdentifier(o2) : o1 === o2;
  }

  addOverviewToCollectionIfMissing<Type extends Pick<IOverview, 'id'>>(
    overviewCollection: Type[],
    ...overviewsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const overviews: Type[] = overviewsToCheck.filter(isPresent);
    if (overviews.length > 0) {
      const overviewCollectionIdentifiers = overviewCollection.map(overviewItem => this.getOverviewIdentifier(overviewItem));
      const overviewsToAdd = overviews.filter(overviewItem => {
        const overviewIdentifier = this.getOverviewIdentifier(overviewItem);
        if (overviewCollectionIdentifiers.includes(overviewIdentifier)) {
          return false;
        }
        overviewCollectionIdentifiers.push(overviewIdentifier);
        return true;
      });
      return [...overviewsToAdd, ...overviewCollection];
    }
    return overviewCollection;
  }

  protected convertDateFromClient<T extends IOverview | NewOverview | PartialUpdateOverview>(overview: T): RestOf<T> {
    return {
      ...overview,
      dateGenerated: overview.dateGenerated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOverview: RestOverview): IOverview {
    return {
      ...restOverview,
      dateGenerated: restOverview.dateGenerated ? dayjs(restOverview.dateGenerated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOverview>): HttpResponse<IOverview> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOverview[]>): HttpResponse<IOverview[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
