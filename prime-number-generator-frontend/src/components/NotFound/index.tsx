import { Alert, Button, Stack } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { Link as RouterLink } from 'react-router'
import type { INotFoundProps } from '../../interfaces/INotFoundProps'

function NotFound({ message }: INotFoundProps) {
  return (
    <Stack spacing={3} sx={{ alignItems: 'flex-start' }}>
      <Button
        component={RouterLink}
        to="/requests"
        startIcon={<ArrowBackIcon />}
      >
        Back to requests
      </Button>
      <Alert severity="warning">{message}</Alert>
    </Stack>
  )
}

export default NotFound
