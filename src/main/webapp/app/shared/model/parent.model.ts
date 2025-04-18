import dayjs from 'dayjs';

export interface IParent {
  id?: number;
  name?: string;
  phone?: string;
  dob?: dayjs.Dayjs | null;
  role?: string;
}

export const defaultValue: Readonly<IParent> = {};
