import dayjs from 'dayjs';
import { IChild } from 'app/shared/model/child.model';
import { IVaccination } from 'app/shared/model/vaccination.model';

export interface IScheduleVaccination {
  id?: number;
  scheduledDate?: dayjs.Dayjs;
  status?: string;
  child?: IChild | null;
  vaccination?: IVaccination | null;
}

export const defaultValue: Readonly<IScheduleVaccination> = {};
