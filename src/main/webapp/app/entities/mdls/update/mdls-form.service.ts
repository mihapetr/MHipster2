import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMDLS, NewMDLS } from '../mdls.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMDLS for edit and NewMDLSFormGroupInput for create.
 */
type MDLSFormGroupInput = IMDLS | PartialWithRequiredKeyOf<NewMDLS>;

type MDLSFormDefaults = Pick<NewMDLS, 'id'>;

type MDLSFormGroupContent = {
  id: FormControl<IMDLS['id'] | NewMDLS['id']>;
  baseConfig: FormControl<IMDLS['baseConfig']>;
  content: FormControl<IMDLS['content']>;
  user: FormControl<IMDLS['user']>;
};

export type MDLSFormGroup = FormGroup<MDLSFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MDLSFormService {
  createMDLSFormGroup(mDLS: MDLSFormGroupInput = { id: null }): MDLSFormGroup {
    const mDLSRawValue = {
      ...this.getFormDefaults(),
      ...mDLS,
    };
    return new FormGroup<MDLSFormGroupContent>({
      id: new FormControl(
        { value: mDLSRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      baseConfig: new FormControl(mDLSRawValue.baseConfig),
      content: new FormControl(mDLSRawValue.content),
      user: new FormControl(mDLSRawValue.user, {
        //validators: [Validators.required],
      }),
    });
  }

  getMDLS(form: MDLSFormGroup): IMDLS | NewMDLS {
    form.patchValue({
      user: { id: 1 },
    });
    return form.getRawValue() as IMDLS | NewMDLS;
  }

  resetForm(form: MDLSFormGroup, mDLS: MDLSFormGroupInput): void {
    const mDLSRawValue = { ...this.getFormDefaults(), ...mDLS };
    form.reset(
      {
        ...mDLSRawValue,
        id: { value: mDLSRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MDLSFormDefaults {
    return {
      id: null,
    };
  }
}
