import dayjs from 'dayjs/esm';

import { IFeatureTst, NewFeatureTst } from './feature-tst.model';

export const sampleWithRequiredData: IFeatureTst = {
  id: 30944,
};

export const sampleWithPartialData: IFeatureTst = {
  id: 3817,
};

export const sampleWithFullData: IFeatureTst = {
  id: 30245,
  date: dayjs('2025-06-26T07:31'),
};

export const sampleWithNewData: NewFeatureTst = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
