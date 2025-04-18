import parent from 'app/entities/parent/parent.reducer';
import healthRecord from 'app/entities/health-record/health-record.reducer';
import child from 'app/entities/child/child.reducer';
import vaccination from 'app/entities/vaccination/vaccination.reducer';
import scheduleVaccination from 'app/entities/schedule-vaccination/schedule-vaccination.reducer';
import healthWorker from 'app/entities/health-worker/health-worker.reducer';
import appointment from 'app/entities/appointment/appointment.reducer';
import feedback from 'app/entities/feedback/feedback.reducer';
import aIAnalyzer from 'app/entities/ai-analyzer/ai-analyzer.reducer';
import reminder from 'app/entities/reminder/reminder.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  parent,
  healthRecord,
  child,
  vaccination,
  scheduleVaccination,
  healthWorker,
  appointment,
  feedback,
  aIAnalyzer,
  reminder,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
