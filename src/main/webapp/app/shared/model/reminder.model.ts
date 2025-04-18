import { IAppointment } from 'app/shared/model/appointment.model';
import { IParent } from 'app/shared/model/parent.model';

export interface IReminder {
  id?: number;
  messageText?: string;
  appointment?: IAppointment | null;
  recipient?: IParent | null;
}

export const defaultValue: Readonly<IReminder> = {};
