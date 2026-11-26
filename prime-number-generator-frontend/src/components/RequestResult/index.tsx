import { Alert, Box, Chip, Paper, Stack, Typography } from '@mui/material'
import type { IRequestResultProps } from '../../interfaces/IRequestResultProps'
import { formatDate } from '../../utils/formatDate'

function RequestResult({ processingResult }: IRequestResultProps) {
  return (
    <Paper variant="outlined" sx={{ p: 3 }}>
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Result
      </Typography>
      {processingResult.state === 'IN_QUEUE' && (
        <Alert severity="info">
          Your request is in the queue and will be processed shortly.
        </Alert>
      )}
      {processingResult.state === 'PROCESSING' && (
        <Alert severity="info">
          The request is being processed
          {processingResult.startedAt
            ? ` since ${formatDate(processingResult.startedAt)}.`
            : '.'}
        </Alert>
      )}
      {processingResult.state === 'FAIL' && (
        <Alert severity="error">
          {processingResult.errorMessage ??
            'Could not complete processing.'}
        </Alert>
      )}
      {processingResult.state === 'SUCCESS' && (
        <Stack spacing={2}>
          <Typography color="text.secondary">
            {processingResult.results?.length ?? 0} prime numbers found
            in the range.
          </Typography>
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
            {processingResult.results?.map((prime) => (
              <Chip key={String(prime)} label={prime} variant="outlined" />
            ))}
          </Box>
          {processingResult.finishedAt && (
            <Typography variant="body2" color="text.secondary">
              Completed at {formatDate(processingResult.finishedAt)}
            </Typography>
          )}
        </Stack>
      )}
    </Paper>
  )
}

export default RequestResult
