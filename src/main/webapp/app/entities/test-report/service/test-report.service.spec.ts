import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITestReport } from '../test-report.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../test-report.test-samples';

import { TestReportService } from './test-report.service';

const requireRestSample: ITestReport = {
  ...sampleWithRequiredData,
};

describe('TestReport Service', () => {
  let service: TestReportService;
  let httpMock: HttpTestingController;
  let expectedResult: ITestReport | ITestReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TestReportService);
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

    it('should create a TestReport', () => {
      const testReport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(testReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TestReport', () => {
      const testReport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(testReport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TestReport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TestReport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TestReport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTestReportToCollectionIfMissing', () => {
      it('should add a TestReport to an empty array', () => {
        const testReport: ITestReport = sampleWithRequiredData;
        expectedResult = service.addTestReportToCollectionIfMissing([], testReport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testReport);
      });

      it('should not add a TestReport to an array that contains it', () => {
        const testReport: ITestReport = sampleWithRequiredData;
        const testReportCollection: ITestReport[] = [
          {
            ...testReport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTestReportToCollectionIfMissing(testReportCollection, testReport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TestReport to an array that doesn't contain it", () => {
        const testReport: ITestReport = sampleWithRequiredData;
        const testReportCollection: ITestReport[] = [sampleWithPartialData];
        expectedResult = service.addTestReportToCollectionIfMissing(testReportCollection, testReport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testReport);
      });

      it('should add only unique TestReport to an array', () => {
        const testReportArray: ITestReport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const testReportCollection: ITestReport[] = [sampleWithRequiredData];
        expectedResult = service.addTestReportToCollectionIfMissing(testReportCollection, ...testReportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testReport: ITestReport = sampleWithRequiredData;
        const testReport2: ITestReport = sampleWithPartialData;
        expectedResult = service.addTestReportToCollectionIfMissing([], testReport, testReport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testReport);
        expect(expectedResult).toContain(testReport2);
      });

      it('should accept null and undefined values', () => {
        const testReport: ITestReport = sampleWithRequiredData;
        expectedResult = service.addTestReportToCollectionIfMissing([], null, testReport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testReport);
      });

      it('should return initial array if no TestReport is added', () => {
        const testReportCollection: ITestReport[] = [sampleWithRequiredData];
        expectedResult = service.addTestReportToCollectionIfMissing(testReportCollection, undefined, null);
        expect(expectedResult).toEqual(testReportCollection);
      });
    });

    describe('compareTestReport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTestReport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 29997 };
        const entity2 = null;

        const compareResult1 = service.compareTestReport(entity1, entity2);
        const compareResult2 = service.compareTestReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 29997 };
        const entity2 = { id: 6462 };

        const compareResult1 = service.compareTestReport(entity1, entity2);
        const compareResult2 = service.compareTestReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 29997 };
        const entity2 = { id: 29997 };

        const compareResult1 = service.compareTestReport(entity1, entity2);
        const compareResult2 = service.compareTestReport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
