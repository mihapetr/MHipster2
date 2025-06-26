import dayjs from 'dayjs/esm';
import { IMDLS } from 'app/entities/mdls/mdls.model';
import { IUser } from 'app/entities/user/user.model';
import { IFeature } from 'app/entities/feature/feature.model';
import { IOverview } from 'app/entities/overview/overview.model';

export interface IProject {
  id: number;
  name?: string | null;
  description?: string | null;
  creationTimeStamp?: dayjs.Dayjs | null;
  location?: string | null;
  mdls?: IMDLS | null;
  user?: Pick<IUser, 'id'> | null;
  features?: IFeature[] | null;
  overviews?: IOverview[] | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
