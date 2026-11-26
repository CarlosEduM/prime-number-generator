import { Box, Paper, Typography } from '@mui/material'
import type { IRequestSummaryProps } from '../../interfaces/IRequestSummaryProps'
import { formatDate } from '../../utils/formatDate'

function RequestSummary({ request }: IRequestSummaryProps) {
  return (
    <Paper variant="outlined" sx={{ p: 3 }}>
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Summary
      </Typography>
      <Box
        sx={{
          display: 'grid',
          gridTemplateColumns: { xs: '1fr', sm: 'repeat(3, 1fr)' },
          gap: 3,
        }}
      >
        <Box>
          <Typography variant="body2" color="text.secondary">
            Requested range
          </Typography>
          <Typography sx={{ fontWeight: 600 }}>
            {request.inputNumberMin} a {request.inputNumberMax}
          </Typography>
        </Box>
        <Box>
          <Typography variant="body2" color="text.secondary">
            Created at
          </Typography>
          <Typography sx={{ fontWeight: 600 }}>
            {formatDate(request.createdAt)}
          </Typography>
        </Box>
        <Box>
          <Typography variant="body2" color="text.secondary">
            Last updated
          </Typography>
          <Typography sx={{ fontWeight: 600 }}>
            {formatDate(request.updatedAt)}
          </Typography>
        </Box>
      </Box>
    </Paper>
  )
}

export default RequestSummary
