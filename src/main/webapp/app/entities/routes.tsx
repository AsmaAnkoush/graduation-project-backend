import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Parent from './parent';
import HealthRecord from './health-record';
import Child from './child';
import Vaccination from './vaccination';
import ScheduleVaccination from './schedule-vaccination';
import HealthWorker from './health-worker';
import Appointment from './appointment';
import Feedback from './feedback';
import AIAnalyzer from './ai-analyzer';
import Reminder from './reminder';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="parent/*" element={<Parent />} />
        <Route path="health-record/*" element={<HealthRecord />} />
        <Route path="child/*" element={<Child />} />
        <Route path="vaccination/*" element={<Vaccination />} />
        <Route path="schedule-vaccination/*" element={<ScheduleVaccination />} />
        <Route path="health-worker/*" element={<HealthWorker />} />
        <Route path="appointment/*" element={<Appointment />} />
        <Route path="feedback/*" element={<Feedback />} />
        <Route path="ai-analyzer/*" element={<AIAnalyzer />} />
        <Route path="reminder/*" element={<Reminder />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
