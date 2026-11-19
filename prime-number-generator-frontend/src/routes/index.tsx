import { Navigate, Route, Routes } from 'react-router'
import LoginPage from '../pages/Login'
import NotFoundPage from '../pages/NotFound'
import RequestDetailsPage from '../pages/RequestDetails'
import RequestsPage from '../pages/Requests'

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/requests" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/requests" element={<RequestsPage />} />
      <Route path="/requests/:requestId" element={<RequestDetailsPage />} />
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}

export default AppRoutes
