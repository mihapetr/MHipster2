import { ITestReport, NewTestReport } from './test-report.model';

export const sampleWithRequiredData: ITestReport = {
  id: 2291,
};

export const sampleWithPartialData: ITestReport = {
  id: 3168,
  branches: 27246,
  missedMethods: 30989,
  missedClasses: 21708,
  classes: 14467,
};

export const sampleWithFullData: ITestReport = {
  id: 11099,
  html: 'through jittery',
  runtimeRetention: true,
  missedInstructions: 10410,
  instructions: 12698,
  missedBranches: 21748,
  branches: 25190,
  missedLines: 28493,
  lines: 23558,
  missedMethods: 9062,
  methods: 7436,
  missedClasses: 21940,
  classes: 27739,
};

export const sampleWithNewData: NewTestReport = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
