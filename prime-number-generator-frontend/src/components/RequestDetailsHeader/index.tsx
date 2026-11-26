import { Box, Button, Chip, Stack, Typography } from '@mui/material'
import type { ChipProps } from '@mui/material/Chip'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { Link as RouterLink } from 'react-router'
import type { IRequestDetailsHeaderProps } from '../../interfaces/IRequestDetailsHeaderProps'
import type { PrimeRequestStatus } from '../../interfaces/IPrimeRequest'

const requestStatusPresentation = {
  PENDING: { label: 'Pending', color: 'warning' },
  PROCESSING: { label: 'Processing', color: 'info' },
  COMPLETED: { label: 'Completed', color: 'success' },
} satisfies Record<
  PrimeRequestStatus,
  { label: string; color: ChipProps['color'] }
>

function RequestDetailsHeader({
  request,
  resultState,
}: IRequestDetailsHeaderProps) {
  const status =
    resultState === 'FAIL'
      ? { label: 'Failed', color: 'error' as const }
      : requestStatusPresentation[request.status]

  return (
    <>
      <Button
        component={RouterLink}
        to="/requests"
        startIcon={<ArrowBackIcon />}
        sx={{ alignSelf: 'flex-start' }}
      >
        Back to requests
      </Button>
      <Stack
        sx={{
          display: 'flex',
          flexDirection: { xs: 'column', sm: 'row' },
          justifyContent: 'space-between',
          alignItems: { xs: 'flex-start', sm: 'center' },
          gap: 1,
        }}
      >
        <Box>
          <Typography component="h1" variant="h4" gutterBottom>
            Request details
          </Typography>
          <Typography
            variant="body2"
            color="text.secondary"
            sx={{ overflowWrap: 'anywhere' }}
          >
            ID: {request.id}
          </Typography>
        </Box>
        <Chip label={status.label} color={status.color} />
      </Stack>
    </>
  )
}

export default RequestDetailsHeader
