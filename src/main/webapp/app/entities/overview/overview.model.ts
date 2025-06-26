import dayjs from 'dayjs/esm';
import { ICodeStats } from 'app/entities/code-stats/code-stats.model';
import { IUser } from 'app/entities/user/user.model';
import { IProject } from 'app/entities/project/project.model';

export interface IOverview {
  id: number;
  dateGenerated?: dayjs.Dayjs | null;
  parent?: ICodeStats | null;
  user?: Pick<IUser, 'id'> | null;
  projects?: IProject[] | null;
}

export type NewOverview = Omit<IOverview, 'id'> & { id: null };
