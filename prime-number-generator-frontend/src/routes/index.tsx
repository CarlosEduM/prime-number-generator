import { Navigate, Route, Routes } from 'react-router'
import LoginPage from '../pages/Login'
import NotFoundPage from '../pages/NotFound'
import RequestDetailsPage from '../pages/RequestDetails'
import RequestsPage from '../pages/Requests'
import ProtectedRoute from '../components/ProtectedRoute'

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/requests" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/requests"
        element={
          <ProtectedRoute>
            <RequestsPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/requests/:requestId"
        element={
          <ProtectedRoute>
            <RequestDetailsPage />
          </ProtectedRoute>
        }
      />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default AppRoutes
