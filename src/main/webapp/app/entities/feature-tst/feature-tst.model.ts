import dayjs from 'dayjs/esm';
import { ICodeStats } from 'app/entities/code-stats/code-stats.model';
import { IFeature } from 'app/entities/feature/feature.model';
import { IProject } from 'app/entities/project/project.model';
import { ITestReport } from '../test-report/test-report.model';

export interface IFeatureTst {
  id: number;
  date?: dayjs.Dayjs | null;
  parent?: ICodeStats | null;
  features?: IFeature[] | null;
  project?: IProject | null;
  testReports?: ITestReport[] | null;
}

export type NewFeatureTst = Omit<IFeatureTst, 'id'> & { id: null };
