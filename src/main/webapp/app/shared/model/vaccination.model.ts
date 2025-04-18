import dayjs from 'dayjs';

export interface IVaccination {
  id?: number;
  name?: string;
  type?: string | null;
  dateGiven?: dayjs.Dayjs | null;
  sideEffects?: string | null;
  targetAge?: number | null;
  status?: string;
  treatment?: string | null;
}

export const defaultValue: Readonly<IVaccination> = {};
