import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFeatureTst } from '../feature-tst.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../feature-tst.test-samples';

import { FeatureTstService, RestFeatureTst } from './feature-tst.service';

const requireRestSample: RestFeatureTst = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('FeatureTst Service', () => {
  let service: FeatureTstService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeatureTst | IFeatureTst[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FeatureTstService);
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

    it('should create a FeatureTst', () => {
      const featureTst = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(featureTst).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeatureTst', () => {
      const featureTst = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(featureTst).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeatureTst', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeatureTst', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeatureTst', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeatureTstToCollectionIfMissing', () => {
      it('should add a FeatureTst to an empty array', () => {
        const featureTst: IFeatureTst = sampleWithRequiredData;
        expectedResult = service.addFeatureTstToCollectionIfMissing([], featureTst);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(featureTst);
      });

      it('should not add a FeatureTst to an array that contains it', () => {
        const featureTst: IFeatureTst = sampleWithRequiredData;
        const featureTstCollection: IFeatureTst[] = [
          {
            ...featureTst,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeatureTstToCollectionIfMissing(featureTstCollection, featureTst);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeatureTst to an array that doesn't contain it", () => {
        const featureTst: IFeatureTst = sampleWithRequiredData;
        const featureTstCollection: IFeatureTst[] = [sampleWithPartialData];
        expectedResult = service.addFeatureTstToCollectionIfMissing(featureTstCollection, featureTst);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(featureTst);
      });

      it('should add only unique FeatureTst to an array', () => {
        const featureTstArray: IFeatureTst[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const featureTstCollection: IFeatureTst[] = [sampleWithRequiredData];
        expectedResult = service.addFeatureTstToCollectionIfMissing(featureTstCollection, ...featureTstArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const featureTst: IFeatureTst = sampleWithRequiredData;
        const featureTst2: IFeatureTst = sampleWithPartialData;
        expectedResult = service.addFeatureTstToCollectionIfMissing([], featureTst, featureTst2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(featureTst);
        expect(expectedResult).toContain(featureTst2);
      });

      it('should accept null and undefined values', () => {
        const featureTst: IFeatureTst = sampleWithRequiredData;
        expectedResult = service.addFeatureTstToCollectionIfMissing([], null, featureTst, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(featureTst);
      });

      it('should return initial array if no FeatureTst is added', () => {
        const featureTstCollection: IFeatureTst[] = [sampleWithRequiredData];
        expectedResult = service.addFeatureTstToCollectionIfMissing(featureTstCollection, undefined, null);
        expect(expectedResult).toEqual(featureTstCollection);
      });
    });

    describe('compareFeatureTst', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeatureTst(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 14938 };
        const entity2 = null;

        const compareResult1 = service.compareFeatureTst(entity1, entity2);
        const compareResult2 = service.compareFeatureTst(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 14938 };
        const entity2 = { id: 24103 };

        const compareResult1 = service.compareFeatureTst(entity1, entity2);
        const compareResult2 = service.compareFeatureTst(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 14938 };
        const entity2 = { id: 14938 };

        const compareResult1 = service.compareFeatureTst(entity1, entity2);
        const compareResult2 = service.compareFeatureTst(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
