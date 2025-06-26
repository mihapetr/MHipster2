import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProject, NewProject } from '../project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProject for edit and NewProjectFormGroupInput for create.
 */
type ProjectFormGroupInput = IProject | PartialWithRequiredKeyOf<NewProject>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProject | NewProject> = Omit<T, 'creationTimeStamp'> & {
  creationTimeStamp?: string | null;
};

type ProjectFormRawValue = FormValueOf<IProject>;

type NewProjectFormRawValue = FormValueOf<NewProject>;

type ProjectFormDefaults = Pick<NewProject, 'id' | 'creationTimeStamp' | 'features' | 'overviews'>;

type ProjectFormGroupContent = {
  id: FormControl<ProjectFormRawValue['id'] | NewProject['id']>;
  name: FormControl<ProjectFormRawValue['name']>;
  description: FormControl<ProjectFormRawValue['description']>;
  creationTimeStamp: FormControl<ProjectFormRawValue['creationTimeStamp']>;
  location: FormControl<ProjectFormRawValue['location']>;
  mdls: FormControl<ProjectFormRawValue['mdls']>;
  user: FormControl<ProjectFormRawValue['user']>;
  features: FormControl<ProjectFormRawValue['features']>;
  overviews: FormControl<ProjectFormRawValue['overviews']>;
};

export type ProjectFormGroup = FormGroup<ProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectFormService {
  createProjectFormGroup(project: ProjectFormGroupInput = { id: null }): ProjectFormGroup {
    const projectRawValue = this.convertProjectToProjectRawValue({
      ...this.getFormDefaults(),
      ...project,
    });
    return new FormGroup<ProjectFormGroupContent>({
      id: new FormControl(
        { value: projectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(projectRawValue.name),
      description: new FormControl(projectRawValue.description),
      creationTimeStamp: new FormControl(projectRawValue.creationTimeStamp),
      location: new FormControl(projectRawValue.location),
      mdls: new FormControl(projectRawValue.mdls),
      user: new FormControl(projectRawValue.user, {
        validators: [Validators.required],
      }),
      features: new FormControl(projectRawValue.features ?? []),
      overviews: new FormControl(projectRawValue.overviews ?? []),
    });
  }

  getProject(form: ProjectFormGroup): IProject | NewProject {
    return this.convertProjectRawValueToProject(form.getRawValue() as ProjectFormRawValue | NewProjectFormRawValue);
  }

  resetForm(form: ProjectFormGroup, project: ProjectFormGroupInput): void {
    const projectRawValue = this.convertProjectToProjectRawValue({ ...this.getFormDefaults(), ...project });
    form.reset(
      {
        ...projectRawValue,
        id: { value: projectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProjectFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationTimeStamp: currentTime,
      features: [],
      overviews: [],
    };
  }

  private convertProjectRawValueToProject(rawProject: ProjectFormRawValue | NewProjectFormRawValue): IProject | NewProject {
    return {
      ...rawProject,
      creationTimeStamp: dayjs(rawProject.creationTimeStamp, DATE_TIME_FORMAT),
    };
  }

  private convertProjectToProjectRawValue(
    project: IProject | (Partial<NewProject> & ProjectFormDefaults),
  ): ProjectFormRawValue | PartialWithRequiredKeyOf<NewProjectFormRawValue> {
    return {
      ...project,
      creationTimeStamp: project.creationTimeStamp ? project.creationTimeStamp.format(DATE_TIME_FORMAT) : undefined,
      features: project.features ?? [],
      overviews: project.overviews ?? [],
    };
  }
}
