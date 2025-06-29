import { ICodeStats, NewCodeStats } from './code-stats.model';

export const sampleWithRequiredData: ICodeStats = {
  id: 28880,
};

export const sampleWithPartialData: ICodeStats = {
  id: 9587,
  instructions: 4982.77,
  methods: 11314.7,
  deadInstructions: 623.78,
  deadLines: 12151.64,
};

export const sampleWithFullData: ICodeStats = {
  id: 17374,
  instructions: 3663.28,
  branches: 31431.96,
  lines: 14527.02,
  methods: 6776.42,
  classes: 23987.67,
  deadInstructions: 8894.92,
  deadBranches: 2394.33,
  deadLines: 14263.9,
  deadMethods: 17866.05,
  deadClasses: 5136.94,
};

export const sampleWithNewData: NewCodeStats = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
