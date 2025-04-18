import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AIAnalyzer from './ai-analyzer';
import AIAnalyzerDetail from './ai-analyzer-detail';
import AIAnalyzerUpdate from './ai-analyzer-update';
import AIAnalyzerDeleteDialog from './ai-analyzer-delete-dialog';

const AIAnalyzerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AIAnalyzer />} />
    <Route path="new" element={<AIAnalyzerUpdate />} />
    <Route path=":id">
      <Route index element={<AIAnalyzerDetail />} />
      <Route path="edit" element={<AIAnalyzerUpdate />} />
      <Route path="delete" element={<AIAnalyzerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AIAnalyzerRoutes;
