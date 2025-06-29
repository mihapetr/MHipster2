import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICodeStats, NewCodeStats } from '../code-stats.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICodeStats for edit and NewCodeStatsFormGroupInput for create.
 */
type CodeStatsFormGroupInput = ICodeStats | PartialWithRequiredKeyOf<NewCodeStats>;

type CodeStatsFormDefaults = Pick<NewCodeStats, 'id'>;

type CodeStatsFormGroupContent = {
  id: FormControl<ICodeStats['id'] | NewCodeStats['id']>;
  instructions: FormControl<ICodeStats['instructions']>;
  branches: FormControl<ICodeStats['branches']>;
  lines: FormControl<ICodeStats['lines']>;
  methods: FormControl<ICodeStats['methods']>;
  classes: FormControl<ICodeStats['classes']>;
  deadInstructions: FormControl<ICodeStats['deadInstructions']>;
  deadBranches: FormControl<ICodeStats['deadBranches']>;
  deadLines: FormControl<ICodeStats['deadLines']>;
  deadMethods: FormControl<ICodeStats['deadMethods']>;
  deadClasses: FormControl<ICodeStats['deadClasses']>;
};

export type CodeStatsFormGroup = FormGroup<CodeStatsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CodeStatsFormService {
  createCodeStatsFormGroup(codeStats: CodeStatsFormGroupInput = { id: null }): CodeStatsFormGroup {
    const codeStatsRawValue = {
      ...this.getFormDefaults(),
      ...codeStats,
    };
    return new FormGroup<CodeStatsFormGroupContent>({
      id: new FormControl(
        { value: codeStatsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      instructions: new FormControl(codeStatsRawValue.instructions),
      branches: new FormControl(codeStatsRawValue.branches),
      lines: new FormControl(codeStatsRawValue.lines),
      methods: new FormControl(codeStatsRawValue.methods),
      classes: new FormControl(codeStatsRawValue.classes),
      deadInstructions: new FormControl(codeStatsRawValue.deadInstructions),
      deadBranches: new FormControl(codeStatsRawValue.deadBranches),
      deadLines: new FormControl(codeStatsRawValue.deadLines),
      deadMethods: new FormControl(codeStatsRawValue.deadMethods),
      deadClasses: new FormControl(codeStatsRawValue.deadClasses),
    });
  }

  getCodeStats(form: CodeStatsFormGroup): ICodeStats | NewCodeStats {
    return form.getRawValue() as ICodeStats | NewCodeStats;
  }

  resetForm(form: CodeStatsFormGroup, codeStats: CodeStatsFormGroupInput): void {
    const codeStatsRawValue = { ...this.getFormDefaults(), ...codeStats };
    form.reset(
      {
        ...codeStatsRawValue,
        id: { value: codeStatsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CodeStatsFormDefaults {
    return {
      id: null,
    };
  }
}
