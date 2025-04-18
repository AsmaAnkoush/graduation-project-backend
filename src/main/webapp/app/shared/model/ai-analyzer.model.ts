import { IFeedback } from 'app/shared/model/feedback.model';

export interface IAIAnalyzer {
  id?: number;
  analysisResult?: string | null;
  feedback?: IFeedback | null;
}

export const defaultValue: Readonly<IAIAnalyzer> = {};
