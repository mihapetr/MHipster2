import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICodeStats } from '../code-stats.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../code-stats.test-samples';

import { CodeStatsService } from './code-stats.service';

const requireRestSample: ICodeStats = {
  ...sampleWithRequiredData,
};

describe('CodeStats Service', () => {
  let service: CodeStatsService;
  let httpMock: HttpTestingController;
  let expectedResult: ICodeStats | ICodeStats[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CodeStatsService);
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

    it('should create a CodeStats', () => {
      const codeStats = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(codeStats).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CodeStats', () => {
      const codeStats = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(codeStats).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CodeStats', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CodeStats', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CodeStats', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCodeStatsToCollectionIfMissing', () => {
      it('should add a CodeStats to an empty array', () => {
        const codeStats: ICodeStats = sampleWithRequiredData;
        expectedResult = service.addCodeStatsToCollectionIfMissing([], codeStats);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeStats);
      });

      it('should not add a CodeStats to an array that contains it', () => {
        const codeStats: ICodeStats = sampleWithRequiredData;
        const codeStatsCollection: ICodeStats[] = [
          {
            ...codeStats,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCodeStatsToCollectionIfMissing(codeStatsCollection, codeStats);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CodeStats to an array that doesn't contain it", () => {
        const codeStats: ICodeStats = sampleWithRequiredData;
        const codeStatsCollection: ICodeStats[] = [sampleWithPartialData];
        expectedResult = service.addCodeStatsToCollectionIfMissing(codeStatsCollection, codeStats);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeStats);
      });

      it('should add only unique CodeStats to an array', () => {
        const codeStatsArray: ICodeStats[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const codeStatsCollection: ICodeStats[] = [sampleWithRequiredData];
        expectedResult = service.addCodeStatsToCollectionIfMissing(codeStatsCollection, ...codeStatsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codeStats: ICodeStats = sampleWithRequiredData;
        const codeStats2: ICodeStats = sampleWithPartialData;
        expectedResult = service.addCodeStatsToCollectionIfMissing([], codeStats, codeStats2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codeStats);
        expect(expectedResult).toContain(codeStats2);
      });

      it('should accept null and undefined values', () => {
        const codeStats: ICodeStats = sampleWithRequiredData;
        expectedResult = service.addCodeStatsToCollectionIfMissing([], null, codeStats, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codeStats);
      });

      it('should return initial array if no CodeStats is added', () => {
        const codeStatsCollection: ICodeStats[] = [sampleWithRequiredData];
        expectedResult = service.addCodeStatsToCollectionIfMissing(codeStatsCollection, undefined, null);
        expect(expectedResult).toEqual(codeStatsCollection);
      });
    });

    describe('compareCodeStats', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCodeStats(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 16198 };
        const entity2 = null;

        const compareResult1 = service.compareCodeStats(entity1, entity2);
        const compareResult2 = service.compareCodeStats(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 16198 };
        const entity2 = { id: 3264 };

        const compareResult1 = service.compareCodeStats(entity1, entity2);
        const compareResult2 = service.compareCodeStats(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 16198 };
        const entity2 = { id: 16198 };

        const compareResult1 = service.compareCodeStats(entity1, entity2);
        const compareResult2 = service.compareCodeStats(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
