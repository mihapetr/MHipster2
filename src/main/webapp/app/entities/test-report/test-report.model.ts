import { IFeatureTst } from 'app/entities/feature-tst/feature-tst.model';

export interface ITestReport {
  id: number;
  html?: string | null;
  runtimeRetention?: boolean | null;
  missedInstructions?: number | null;
  instructions?: number | null;
  missedBranches?: number | null;
  branches?: number | null;
  missedLines?: number | null;
  lines?: number | null;
  missedMethods?: number | null;
  methods?: number | null;
  missedClasses?: number | null;
  classes?: number | null;
  featureTst?: IFeatureTst | null;
}

export type NewTestReport = Omit<ITestReport, 'id'> & { id: null };
