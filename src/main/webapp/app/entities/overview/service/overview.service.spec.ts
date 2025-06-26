import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOverview } from '../overview.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../overview.test-samples';

import { OverviewService, RestOverview } from './overview.service';

const requireRestSample: RestOverview = {
  ...sampleWithRequiredData,
  dateGenerated: sampleWithRequiredData.dateGenerated?.toJSON(),
};

describe('Overview Service', () => {
  let service: OverviewService;
  let httpMock: HttpTestingController;
  let expectedResult: IOverview | IOverview[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OverviewService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Overview', () => {
      const overview = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(overview).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Overview', () => {
      const overview = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(overview).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Overview', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Overview', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Overview', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOverviewToCollectionIfMissing', () => {
      it('should add a Overview to an empty array', () => {
        const overview: IOverview = sampleWithRequiredData;
        expectedResult = service.addOverviewToCollectionIfMissing([], overview);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(overview);
      });

      it('should not add a Overview to an array that contains it', () => {
        const overview: IOverview = sampleWithRequiredData;
        const overviewCollection: IOverview[] = [
          {
            ...overview,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOverviewToCollectionIfMissing(overviewCollection, overview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Overview to an array that doesn't contain it", () => {
        const overview: IOverview = sampleWithRequiredData;
        const overviewCollection: IOverview[] = [sampleWithPartialData];
        expectedResult = service.addOverviewToCollectionIfMissing(overviewCollection, overview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(overview);
      });

      it('should add only unique Overview to an array', () => {
        const overviewArray: IOverview[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const overviewCollection: IOverview[] = [sampleWithRequiredData];
        expectedResult = service.addOverviewToCollectionIfMissing(overviewCollection, ...overviewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const overview: IOverview = sampleWithRequiredData;
        const overview2: IOverview = sampleWithPartialData;
        expectedResult = service.addOverviewToCollectionIfMissing([], overview, overview2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(overview);
        expect(expectedResult).toContain(overview2);
      });

      it('should accept null and undefined values', () => {
        const overview: IOverview = sampleWithRequiredData;
        expectedResult = service.addOverviewToCollectionIfMissing([], null, overview, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(overview);
      });

      it('should return initial array if no Overview is added', () => {
        const overviewCollection: IOverview[] = [sampleWithRequiredData];
        expectedResult = service.addOverviewToCollectionIfMissing(overviewCollection, undefined, null);
        expect(expectedResult).toEqual(overviewCollection);
      });
    });

    describe('compareOverview', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOverview(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 26694 };
        const entity2 = null;

        const compareResult1 = service.compareOverview(entity1, entity2);
        const compareResult2 = service.compareOverview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 26694 };
        const entity2 = { id: 2729 };

        const compareResult1 = service.compareOverview(entity1, entity2);
        const compareResult2 = service.compareOverview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 26694 };
        const entity2 = { id: 26694 };

        const compareResult1 = service.compareOverview(entity1, entity2);
        const compareResult2 = service.compareOverview(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
