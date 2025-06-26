import dayjs from 'dayjs/esm';

import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 22823,
};

export const sampleWithPartialData: IProject = {
  id: 21731,
  description: 'maul busily',
};

export const sampleWithFullData: IProject = {
  id: 1375,
  name: 'tray pack hydrolyze',
  description: 'as',
  creationTimeStamp: dayjs('2025-06-25T15:41'),
  location: 'pillory painfully supposing',
};

export const sampleWithNewData: NewProject = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
