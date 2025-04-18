export interface IHealthRecord {
  id?: number;
  sensitivity?: string | null;
  diabetes?: boolean | null;
  highBloodPressure?: boolean | null;
  geneticDiseases?: string | null;
  bloodType?: string | null;
}

export const defaultValue: Readonly<IHealthRecord> = {
  diabetes: false,
  highBloodPressure: false,
};
