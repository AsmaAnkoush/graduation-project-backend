import dayjs from 'dayjs';
import { IHealthRecord } from 'app/shared/model/health-record.model';
import { IParent } from 'app/shared/model/parent.model';

export interface IChild {
  id?: number;
  name?: string;
  dob?: dayjs.Dayjs;
  weight?: number | null;
  height?: number | null;
  healthRecord?: IHealthRecord | null;
  parent?: IParent | null;
}

export const defaultValue: Readonly<IChild> = {};
