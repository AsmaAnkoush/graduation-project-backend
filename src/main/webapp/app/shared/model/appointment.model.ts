import dayjs from 'dayjs';
import { IParent } from 'app/shared/model/parent.model';
import { IChild } from 'app/shared/model/child.model';
import { IScheduleVaccination } from 'app/shared/model/schedule-vaccination.model';
import { IHealthWorker } from 'app/shared/model/health-worker.model';

export interface IAppointment {
  id?: number;
  appointmentDate?: dayjs.Dayjs;
  status?: string;
  parent?: IParent | null;
  child?: IChild | null;
  schedule?: IScheduleVaccination | null;
  healthWorker?: IHealthWorker | null;
}

export const defaultValue: Readonly<IAppointment> = {};
