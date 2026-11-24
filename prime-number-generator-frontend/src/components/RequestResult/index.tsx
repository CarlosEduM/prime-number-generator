import { Alert, Box, Chip, Paper, Stack, Typography } from '@mui/material'
import type { IRequestResultProps } from '../../interfaces/IRequestResultProps'
import { formatDate } from '../../utils/formatDate'

function RequestResult({ processingResult }: IRequestResultProps) {
  return (
    <Paper variant="outlined" sx={{ p: 3 }}>
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Resultado
      </Typography>
      {processingResult.state === 'IN_QUEUE' && (
        <Alert severity="info">
          Sua solicitação está na fila e será processada em breve.
        </Alert>
      )}
      {processingResult.state === 'PROCESSING' && (
        <Alert severity="info">
          A solicitação está sendo processada
          {processingResult.startedAt
            ? ` desde ${formatDate(processingResult.startedAt)}.`
            : '.'}
        </Alert>
      )}
      {processingResult.state === 'FAIL' && (
        <Alert severity="error">
          {processingResult.errorMessage ??
            'Não foi possível concluir o processamento.'}
        </Alert>
      )}
      {processingResult.state === 'SUCCESS' && (
        <Stack spacing={2}>
          <Typography color="text.secondary">
            {processingResult.results?.length ?? 0} números primos encontrados
            no intervalo.
          </Typography>
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
            {processingResult.results?.map((prime) => (
              <Chip key={prime} label={prime} variant="outlined" />
            ))}
          </Box>
          {processingResult.finishedAt && (
            <Typography variant="body2" color="text.secondary">
              Concluída em {formatDate(processingResult.finishedAt)}
            </Typography>
          )}
        </Stack>
      )}
    </Paper>
  )
}

export default RequestResult
