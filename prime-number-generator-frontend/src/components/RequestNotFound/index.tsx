import { Alert, Button, Stack } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { Link as RouterLink } from 'react-router'

function RequestNotFound() {
  return (
    <Stack spacing={3}>
      <Button
        component={RouterLink}
        to="/requests"
        startIcon={<ArrowBackIcon />}
        sx={{ alignSelf: 'flex-start' }}
      >
        Voltar para processos
      </Button>
      <Alert severity="warning">
        Não encontramos uma solicitação com esse identificador.
      </Alert>
    </Stack>
  )
}

export default RequestNotFound
