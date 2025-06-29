export interface ICodeStats {
  id: number;
  instructions?: number | null;
  branches?: number | null;
  lines?: number | null;
  methods?: number | null;
  classes?: number | null;
  deadInstructions?: number | null;
  deadBranches?: number | null;
  deadLines?: number | null;
  deadMethods?: number | null;
  deadClasses?: number | null;
}

export type NewCodeStats = Omit<ICodeStats, 'id'> & { id: null };
