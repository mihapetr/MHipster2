import { ICodeStats, NewCodeStats } from './code-stats.model';

export const sampleWithRequiredData: ICodeStats = {
  id: 28880,
};

export const sampleWithPartialData: ICodeStats = {
  id: 13472,
  instructions: 20693.9,
  methods: 15119.08,
};

export const sampleWithFullData: ICodeStats = {
  id: 17374,
  instructions: 3663.28,
  branches: 31431.96,
  lines: 14527.02,
  methods: 6776.42,
  classes: 23987.67,
};

export const sampleWithNewData: NewCodeStats = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
