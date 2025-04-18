import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ScheduleVaccination from './schedule-vaccination';
import ScheduleVaccinationDetail from './schedule-vaccination-detail';
import ScheduleVaccinationUpdate from './schedule-vaccination-update';
import ScheduleVaccinationDeleteDialog from './schedule-vaccination-delete-dialog';

const ScheduleVaccinationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ScheduleVaccination />} />
    <Route path="new" element={<ScheduleVaccinationUpdate />} />
    <Route path=":id">
      <Route index element={<ScheduleVaccinationDetail />} />
      <Route path="edit" element={<ScheduleVaccinationUpdate />} />
      <Route path="delete" element={<ScheduleVaccinationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ScheduleVaccinationRoutes;
