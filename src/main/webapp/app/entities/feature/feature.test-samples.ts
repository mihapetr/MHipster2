import { IFeature, NewFeature } from './feature.model';

export const sampleWithRequiredData: IFeature = {
  id: 26173,
};

export const sampleWithPartialData: IFeature = {
  id: 19648,
  name: 'since',
  content: 'drat prejudge',
};

export const sampleWithFullData: IFeature = {
  id: 31204,
  name: 'breakable',
  content: 'oh how',
};

export const sampleWithNewData: NewFeature = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
