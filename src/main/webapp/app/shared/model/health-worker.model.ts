export interface IHealthWorker {
  id?: number;
  username?: string;
  password?: string;
  phone?: string;
  age?: number | null;
  name?: string;
  gender?: string | null;
  location?: string | null;
  email?: string;
  role?: string;
}

export const defaultValue: Readonly<IHealthWorker> = {};
