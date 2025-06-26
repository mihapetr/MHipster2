import { IUser } from 'app/entities/user/user.model';

export interface IMDLS {
  id: number;
  baseConfig?: string | null;
  content?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewMDLS = Omit<IMDLS, 'id'> & { id: null };
