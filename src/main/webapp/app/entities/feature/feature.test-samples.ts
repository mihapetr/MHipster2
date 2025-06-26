import { IFeature, NewFeature } from './feature.model';

export const sampleWithRequiredData: IFeature = {
  id: 26173,
};

export const sampleWithPartialData: IFeature = {
  id: 19648,
  name: 'since',
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IFeature = {
  id: 31204,
  name: 'breakable',
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewFeature = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
