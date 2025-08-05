import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOverview, NewOverview } from '../overview.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOverview for edit and NewOverviewFormGroupInput for create.
 */
type OverviewFormGroupInput = IOverview | PartialWithRequiredKeyOf<NewOverview>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOverview | NewOverview> = Omit<T, 'dateGenerated'> & {
  dateGenerated?: string | null;
};

type OverviewFormRawValue = FormValueOf<IOverview>;

type NewOverviewFormRawValue = FormValueOf<NewOverview>;

type OverviewFormDefaults = Pick<NewOverview, 'id' | 'dateGenerated' | 'projects'>;

type OverviewFormGroupContent = {
  id: FormControl<OverviewFormRawValue['id'] | NewOverview['id']>;
  dateGenerated: FormControl<OverviewFormRawValue['dateGenerated']>;
  parent: FormControl<OverviewFormRawValue['parent']>;
  user: FormControl<OverviewFormRawValue['user']>;
  projects: FormControl<OverviewFormRawValue['projects']>;
};

export type OverviewFormGroup = FormGroup<OverviewFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OverviewFormService {
  createOverviewFormGroup(overview: OverviewFormGroupInput = { id: null }): OverviewFormGroup {
    const overviewRawValue = this.convertOverviewToOverviewRawValue({
      ...this.getFormDefaults(),
      ...overview,
    });
    return new FormGroup<OverviewFormGroupContent>({
      id: new FormControl(
        { value: overviewRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateGenerated: new FormControl(overviewRawValue.dateGenerated),
      parent: new FormControl(overviewRawValue.parent, {
        // validators: [Validators.required],
      }),
      user: new FormControl(overviewRawValue.user, {
        // validators: [Validators.required],
      }),
      projects: new FormControl(overviewRawValue.projects ?? []),
    });
  }

  getOverview(form: OverviewFormGroup): IOverview | NewOverview {
    form.patchValue({
      user: { id: 1 }, // irrelevant, assigned on server
    });
    return this.convertOverviewRawValueToOverview(form.getRawValue() as OverviewFormRawValue | NewOverviewFormRawValue);
  }

  resetForm(form: OverviewFormGroup, overview: OverviewFormGroupInput): void {
    const overviewRawValue = this.convertOverviewToOverviewRawValue({ ...this.getFormDefaults(), ...overview });
    form.reset(
      {
        ...overviewRawValue,
        id: { value: overviewRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OverviewFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateGenerated: currentTime,
      projects: [],
    };
  }

  private convertOverviewRawValueToOverview(rawOverview: OverviewFormRawValue | NewOverviewFormRawValue): IOverview | NewOverview {
    return {
      ...rawOverview,
      dateGenerated: dayjs(rawOverview.dateGenerated, DATE_TIME_FORMAT),
    };
  }

  private convertOverviewToOverviewRawValue(
    overview: IOverview | (Partial<NewOverview> & OverviewFormDefaults),
  ): OverviewFormRawValue | PartialWithRequiredKeyOf<NewOverviewFormRawValue> {
    return {
      ...overview,
      dateGenerated: overview.dateGenerated ? overview.dateGenerated.format(DATE_TIME_FORMAT) : undefined,
      projects: overview.projects ?? [],
    };
  }
}
