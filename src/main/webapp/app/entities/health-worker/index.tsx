import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import HealthWorker from './health-worker';
import HealthWorkerDetail from './health-worker-detail';
import HealthWorkerUpdate from './health-worker-update';
import HealthWorkerDeleteDialog from './health-worker-delete-dialog';

const HealthWorkerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HealthWorker />} />
    <Route path="new" element={<HealthWorkerUpdate />} />
    <Route path=":id">
      <Route index element={<HealthWorkerDetail />} />
      <Route path="edit" element={<HealthWorkerUpdate />} />
      <Route path="delete" element={<HealthWorkerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HealthWorkerRoutes;
