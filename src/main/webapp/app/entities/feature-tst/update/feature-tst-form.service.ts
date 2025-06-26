import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeatureTst, NewFeatureTst } from '../feature-tst.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeatureTst for edit and NewFeatureTstFormGroupInput for create.
 */
type FeatureTstFormGroupInput = IFeatureTst | PartialWithRequiredKeyOf<NewFeatureTst>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFeatureTst | NewFeatureTst> = Omit<T, 'date'> & {
  date?: string | null;
};

type FeatureTstFormRawValue = FormValueOf<IFeatureTst>;

type NewFeatureTstFormRawValue = FormValueOf<NewFeatureTst>;

type FeatureTstFormDefaults = Pick<NewFeatureTst, 'id' | 'date' | 'features'>;

type FeatureTstFormGroupContent = {
  id: FormControl<FeatureTstFormRawValue['id'] | NewFeatureTst['id']>;
  date: FormControl<FeatureTstFormRawValue['date']>;
  parent: FormControl<FeatureTstFormRawValue['parent']>;
  features: FormControl<FeatureTstFormRawValue['features']>;
  project: FormControl<FeatureTstFormRawValue['project']>;
};

export type FeatureTstFormGroup = FormGroup<FeatureTstFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeatureTstFormService {
  createFeatureTstFormGroup(featureTst: FeatureTstFormGroupInput = { id: null }): FeatureTstFormGroup {
    const featureTstRawValue = this.convertFeatureTstToFeatureTstRawValue({
      ...this.getFormDefaults(),
      ...featureTst,
    });
    return new FormGroup<FeatureTstFormGroupContent>({
      id: new FormControl(
        { value: featureTstRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(featureTstRawValue.date),
      parent: new FormControl(featureTstRawValue.parent, {
        validators: [Validators.required],
      }),
      features: new FormControl(featureTstRawValue.features ?? []),
      project: new FormControl(featureTstRawValue.project, {
        validators: [Validators.required],
      }),
    });
  }

  getFeatureTst(form: FeatureTstFormGroup): IFeatureTst | NewFeatureTst {
    return this.convertFeatureTstRawValueToFeatureTst(form.getRawValue() as FeatureTstFormRawValue | NewFeatureTstFormRawValue);
  }

  resetForm(form: FeatureTstFormGroup, featureTst: FeatureTstFormGroupInput): void {
    const featureTstRawValue = this.convertFeatureTstToFeatureTstRawValue({ ...this.getFormDefaults(), ...featureTst });
    form.reset(
      {
        ...featureTstRawValue,
        id: { value: featureTstRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeatureTstFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      features: [],
    };
  }

  private convertFeatureTstRawValueToFeatureTst(
    rawFeatureTst: FeatureTstFormRawValue | NewFeatureTstFormRawValue,
  ): IFeatureTst | NewFeatureTst {
    return {
      ...rawFeatureTst,
      date: dayjs(rawFeatureTst.date, DATE_TIME_FORMAT),
    };
  }

  private convertFeatureTstToFeatureTstRawValue(
    featureTst: IFeatureTst | (Partial<NewFeatureTst> & FeatureTstFormDefaults),
  ): FeatureTstFormRawValue | PartialWithRequiredKeyOf<NewFeatureTstFormRawValue> {
    return {
      ...featureTst,
      date: featureTst.date ? featureTst.date.format(DATE_TIME_FORMAT) : undefined,
      features: featureTst.features ?? [],
    };
  }
}
