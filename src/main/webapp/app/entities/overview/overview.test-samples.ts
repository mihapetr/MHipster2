import dayjs from 'dayjs/esm';

import { IOverview, NewOverview } from './overview.model';

export const sampleWithRequiredData: IOverview = {
  id: 18502,
};

export const sampleWithPartialData: IOverview = {
  id: 12187,
};

export const sampleWithFullData: IOverview = {
  id: 10916,
  dateGenerated: dayjs('2025-06-26T07:35'),
};

export const sampleWithNewData: NewOverview = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
