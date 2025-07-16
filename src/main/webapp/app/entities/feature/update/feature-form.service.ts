import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFeature, NewFeature } from '../feature.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeature for edit and NewFeatureFormGroupInput for create.
 */
type FeatureFormGroupInput = IFeature | PartialWithRequiredKeyOf<NewFeature>;

type FeatureFormDefaults = Pick<NewFeature, 'id' | 'projects' | 'featureTsts'>;

type FeatureFormGroupContent = {
  id: FormControl<IFeature['id'] | NewFeature['id']>;
  name: FormControl<IFeature['name']>;
  content: FormControl<IFeature['content']>;
  user: FormControl<IFeature['user']>;
  projects: FormControl<IFeature['projects']>;
  featureTsts: FormControl<IFeature['featureTsts']>;
};

export type FeatureFormGroup = FormGroup<FeatureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeatureFormService {
  createFeatureFormGroup(feature: FeatureFormGroupInput = { id: null }): FeatureFormGroup {
    const featureRawValue = {
      ...this.getFormDefaults(),
      ...feature,
    };
    return new FormGroup<FeatureFormGroupContent>({
      id: new FormControl(
        { value: featureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(featureRawValue.name),
      content: new FormControl(featureRawValue.content),
      user: new FormControl(featureRawValue.user, {
        //validators: [Validators.required],
      }),
      projects: new FormControl(featureRawValue.projects ?? []),
      featureTsts: new FormControl(featureRawValue.featureTsts ?? []),
    });
  }

  getFeature(form: FeatureFormGroup): IFeature | NewFeature {
    form.patchValue({
      user: { id: 1 },
    });
    return form.getRawValue() as IFeature | NewFeature;
  }

  resetForm(form: FeatureFormGroup, feature: FeatureFormGroupInput): void {
    const featureRawValue = { ...this.getFormDefaults(), ...feature };
    form.reset(
      {
        ...featureRawValue,
        id: { value: featureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeatureFormDefaults {
    return {
      id: null,
      projects: [],
      featureTsts: [],
    };
  }
}
