import { Navigate, useLocation } from 'react-router'
import type { IProtectedRouteProps } from '../../interfaces/IProtectedRouteProps'
import { useAuth } from '../../hooks/useAuth'

function ProtectedRoute({ children }: IProtectedRouteProps) {
  const { idToken } = useAuth()
  const location = useLocation()

  if (!idToken) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  return children
}

export default ProtectedRoute
