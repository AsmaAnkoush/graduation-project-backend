import dayjs from 'dayjs';
import { IParent } from 'app/shared/model/parent.model';
import { IVaccination } from 'app/shared/model/vaccination.model';

export interface IFeedback {
  id?: number;
  messageText?: string;
  sideEffects?: string | null;
  treatment?: string | null;
  dateSubmitted?: dayjs.Dayjs | null;
  parent?: IParent | null;
  vaccination?: IVaccination | null;
}

export const defaultValue: Readonly<IFeedback> = {};
