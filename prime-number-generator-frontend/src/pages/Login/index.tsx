import LoginIcon from '@mui/icons-material/Login'
import FunctionsIcon from '@mui/icons-material/Functions'
import { GoogleLogin } from '@react-oauth/google'
import {
  Alert,
  Box,
  Container,
  Paper,
  Stack,
  Typography,
} from '@mui/material'
import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router'
import type { ILoginRouteState } from '../../interfaces/ILoginRouteState'
import { useAuth } from '../../hooks/useAuth'

function LoginPage() {
  const [loginError, setLoginError] = useState('')
  const { idToken, signIn } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()
  const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID
  const returnTo =
    (location.state as ILoginRouteState | null)?.from?.pathname ?? '/requests'

  if (idToken) {
    return <Navigate to="/requests" replace />
  }

  return (
    <Container
      maxWidth={false}
      sx={{
        minHeight: '100vh',
        display: 'grid',
        placeItems: 'center',
        py: 4,
        background:
          'radial-gradient(circle at top left, rgba(25, 118, 210, 0.12), transparent 38%)',
      }}
    >
      <Paper
        variant="outlined"
        sx={{
          width: '100%',
          maxWidth: 460,
          p: { xs: 3, sm: 5 },
          borderRadius: 3,
          boxShadow: '0 12px 40px rgba(0, 0, 0, 0.08)',
        }}
      >
        <Stack spacing={3} sx={{ alignItems: 'center', textAlign: 'center' }}>
          <Box
            aria-hidden="true"
            sx={{
              display: 'grid',
              placeItems: 'center',
              width: 56,
              height: 56,
              borderRadius: 2,
              color: 'primary.main',
              bgcolor: 'action.selected',
            }}
          >
            <FunctionsIcon fontSize="large" />
          </Box>
          <Box>
            <Typography component="h1" variant="h4" sx={{ mb: 1 }}>
              Gerador de números primos
            </Typography>
            <Typography color="text.secondary">
              Entre para acompanhar suas solicitações e resultados.
            </Typography>
          </Box>
          <Alert
            severity={googleClientId ? 'info' : 'warning'}
            sx={{ width: '100%', textAlign: 'left' }}
          >
            {googleClientId
              ? 'Você será autenticado com sua conta Google. Nenhuma senha é solicitada por esta aplicação.'
              : 'Configure VITE_GOOGLE_CLIENT_ID no arquivo .env.local para habilitar o login Google.'}
          </Alert>
          {loginError && (
            <Alert severity="error" sx={{ width: '100%', textAlign: 'left' }}>
              {loginError}
            </Alert>
          )}
          {googleClientId && (
            <Box sx={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
              <GoogleLogin
                width="270"
                text="continue_with"
                onSuccess={(response) => {
                  if (!response.credential) {
                    setLoginError(
                      'O Google não retornou uma credencial de autenticação.',
                    )
                    return
                  }

                  signIn(response.credential)
                  navigate(returnTo, { replace: true })
                }}
                onError={() => {
                  setLoginError(
                    'Não foi possível iniciar o login com o Google. Tente novamente.',
                  )
                }}
              />
            </Box>
          )}
          <Stack
            direction="row"
            spacing={1}
            sx={{ alignItems: 'center', color: 'text.secondary' }}
          >
            <LoginIcon fontSize="small" />
            <Typography variant="caption">
              A autenticação é feita com segurança pelo Google.
            </Typography>
          </Stack>
        </Stack>
      </Paper>
    </Container>
  )
}

export default LoginPage
