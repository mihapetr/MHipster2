import { IUser } from 'app/entities/user/user.model';
import { IProject } from 'app/entities/project/project.model';
import { IFeatureTst } from 'app/entities/feature-tst/feature-tst.model';

export interface IFeature {
  id: number;
  name?: string | null;
  content?: string | null;
  user?: Pick<IUser, 'id'> | null;
  projects?: IProject[] | null;
  featureTsts?: IFeatureTst[] | null;
}

export type NewFeature = Omit<IFeature, 'id'> & { id: null };
