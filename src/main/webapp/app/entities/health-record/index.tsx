import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HealthRecord from './health-record';
import HealthRecordDetail from './health-record-detail';
import HealthRecordUpdate from './health-record-update';
import HealthRecordDeleteDialog from './health-record-delete-dialog';

const HealthRecordRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HealthRecord />} />
    <Route path="new" element={<HealthRecordUpdate />} />
    <Route path=":id">
      <Route index element={<HealthRecordDetail />} />
      <Route path="edit" element={<HealthRecordUpdate />} />
      <Route path="delete" element={<HealthRecordDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HealthRecordRoutes;
