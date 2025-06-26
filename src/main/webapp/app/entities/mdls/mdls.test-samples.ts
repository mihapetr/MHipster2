import { IMDLS, NewMDLS } from './mdls.model';

export const sampleWithRequiredData: IMDLS = {
  id: 7266,
};

export const sampleWithPartialData: IMDLS = {
  id: 6213,
  baseConfig: 'pressure whereas',
};

export const sampleWithFullData: IMDLS = {
  id: 10304,
  baseConfig: 'pricey ick',
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewMDLS = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
