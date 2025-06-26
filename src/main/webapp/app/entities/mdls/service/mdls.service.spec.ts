import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMDLS } from '../mdls.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../mdls.test-samples';

import { MDLSService } from './mdls.service';

const requireRestSample: IMDLS = {
  ...sampleWithRequiredData,
};

describe('MDLS Service', () => {
  let service: MDLSService;
  let httpMock: HttpTestingController;
  let expectedResult: IMDLS | IMDLS[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MDLSService);
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

    it('should create a MDLS', () => {
      const mDLS = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mDLS).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MDLS', () => {
      const mDLS = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mDLS).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MDLS', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MDLS', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MDLS', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMDLSToCollectionIfMissing', () => {
      it('should add a MDLS to an empty array', () => {
        const mDLS: IMDLS = sampleWithRequiredData;
        expectedResult = service.addMDLSToCollectionIfMissing([], mDLS);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mDLS);
      });

      it('should not add a MDLS to an array that contains it', () => {
        const mDLS: IMDLS = sampleWithRequiredData;
        const mDLSCollection: IMDLS[] = [
          {
            ...mDLS,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMDLSToCollectionIfMissing(mDLSCollection, mDLS);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MDLS to an array that doesn't contain it", () => {
        const mDLS: IMDLS = sampleWithRequiredData;
        const mDLSCollection: IMDLS[] = [sampleWithPartialData];
        expectedResult = service.addMDLSToCollectionIfMissing(mDLSCollection, mDLS);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mDLS);
      });

      it('should add only unique MDLS to an array', () => {
        const mDLSArray: IMDLS[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mDLSCollection: IMDLS[] = [sampleWithRequiredData];
        expectedResult = service.addMDLSToCollectionIfMissing(mDLSCollection, ...mDLSArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mDLS: IMDLS = sampleWithRequiredData;
        const mDLS2: IMDLS = sampleWithPartialData;
        expectedResult = service.addMDLSToCollectionIfMissing([], mDLS, mDLS2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mDLS);
        expect(expectedResult).toContain(mDLS2);
      });

      it('should accept null and undefined values', () => {
        const mDLS: IMDLS = sampleWithRequiredData;
        expectedResult = service.addMDLSToCollectionIfMissing([], null, mDLS, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mDLS);
      });

      it('should return initial array if no MDLS is added', () => {
        const mDLSCollection: IMDLS[] = [sampleWithRequiredData];
        expectedResult = service.addMDLSToCollectionIfMissing(mDLSCollection, undefined, null);
        expect(expectedResult).toEqual(mDLSCollection);
      });
    });

    describe('compareMDLS', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMDLS(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 15572 };
        const entity2 = null;

        const compareResult1 = service.compareMDLS(entity1, entity2);
        const compareResult2 = service.compareMDLS(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 15572 };
        const entity2 = { id: 18176 };

        const compareResult1 = service.compareMDLS(entity1, entity2);
        const compareResult2 = service.compareMDLS(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 15572 };
        const entity2 = { id: 15572 };

        const compareResult1 = service.compareMDLS(entity1, entity2);
        const compareResult2 = service.compareMDLS(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
