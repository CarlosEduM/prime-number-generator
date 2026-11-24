import { Container, Paper, Stack, Typography } from '@mui/material'
import RequestsTable from '../../components/RequestsTable'
import type { IPrimeRequest } from '../../interfaces/IPrimeRequest'

const mockRequests: IPrimeRequest[] = [
  {
    id: '2a54d46e-ffdc-4df8-9968-f0c93a137541',
    inputNumberMin: '1',
    inputNumberMax: '100',
    status: 'COMPLETED',
    createdAt: '2026-11-18T14:30:00',
    updatedAt: '2026-11-18T14:30:05',
  },
  {
    id: '10943fad-1a1b-42f3-914f-dbd9b41e4be6',
    inputNumberMin: '1000',
    inputNumberMax: '5000',
    status: 'PROCESSING',
    createdAt: '2026-11-19T10:15:00',
    updatedAt: '2026-11-19T10:15:01',
  },
  {
    id: '6a391b08-df9a-45b6-9df5-bca662515080',
    inputNumberMin: '10000',
    inputNumberMax: '20000',
    status: 'PENDING',
    createdAt: '2026-11-19T10:20:00',
    updatedAt: '2026-11-19T10:20:00',
  },
  {
    id: '2a54d46e-ffdc-4df8-9968-f0c93a137542',
    inputNumberMin: '1',
    inputNumberMax: '100',
    status: 'COMPLETED',
    createdAt: '2026-11-18T14:30:00',
    updatedAt: '2026-11-18T14:30:05',
  },
  {
    id: '10943fad-1a1b-42f3-914f-dbd9b41e4be7',
    inputNumberMin: '1000',
    inputNumberMax: '5000',
    status: 'PROCESSING',
    createdAt: '2026-11-19T10:15:00',
    updatedAt: '2026-11-19T10:15:01',
  },
  {
    id: '6a391b08-df9a-45b6-9df5-bca662515081',
    inputNumberMin: '10000',
    inputNumberMax: '20000',
    status: 'PENDING',
    createdAt: '2026-11-19T10:20:00',
    updatedAt: '2026-11-19T10:20:00',
  },
  {
    id: '2a54d46e-ffdc-4df8-9968-f0c93a137543',
    inputNumberMin: '1',
    inputNumberMax: '100',
    status: 'COMPLETED',
    createdAt: '2026-11-18T14:30:00',
    updatedAt: '2026-11-18T14:30:05',
  },
  {
    id: '10943fad-1a1b-42f3-914f-dbd9b41e4be8',
    inputNumberMin: '1000',
    inputNumberMax: '5000',
    status: 'PROCESSING',
    createdAt: '2026-11-19T10:15:00',
    updatedAt: '2026-11-19T10:15:01',
  },
  {
    id: '6a391b08-df9a-45b6-9df5-bca662515082',
    inputNumberMin: '10000',
    inputNumberMax: '20000',
    status: 'PENDING',
    createdAt: '2026-11-19T10:20:00',
    updatedAt: '2026-11-19T10:20:00',
  },
  {
    id: '2a54d46e-ffdc-4df8-9968-f0c93a137544',
    inputNumberMin: '1',
    inputNumberMax: '100',
    status: 'COMPLETED',
    createdAt: '2026-11-18T14:30:00',
    updatedAt: '2026-11-18T14:30:05',
  },
  {
    id: '10943fad-1a1b-42f3-914f-dbd9b41e4be9',
    inputNumberMin: '1000',
    inputNumberMax: '5000',
    status: 'PROCESSING',
    createdAt: '2026-11-19T10:15:00',
    updatedAt: '2026-11-19T10:15:01',
  },
  {
    id: '6a391b08-df9a-45b6-9df5-bca662515083',
    inputNumberMin: '10000',
    inputNumberMax: '20000',
    status: 'PENDING',
    createdAt: '2026-11-19T10:20:00',
    updatedAt: '2026-11-19T10:20:00',
  },
]

function RequestsPage() {
  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <Stack spacing={1} sx={{ mb: 3 }}>
        <Typography component="h1" variant="h4">
          Processos
        </Typography>
        <Typography color="text.secondary">
          Acompanhe suas solicitações de geração de números primos.
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Dados de demonstração: a integração com o orquestrador será feita em
          uma próxima etapa.
        </Typography>
      </Stack>
      <Paper variant="outlined">
        <RequestsTable requests={mockRequests} />
      </Paper>
    </Container>
  )
}

export default RequestsPage
