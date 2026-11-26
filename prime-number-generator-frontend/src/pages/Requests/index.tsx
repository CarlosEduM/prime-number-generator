import { useState } from 'react'
import {
  Button,
  Container,
  Paper,
  Stack,
  Typography,
} from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import CreateRequestDialog from '../../components/CreateRequestDialog'
import RequestsTable from '../../components/RequestsTable'
import type { IPrimeRequest } from '../../interfaces/IPrimeRequest'
import { mockRequests } from '../../mocks/primeRequests'

function RequestsPage() {
  const [requests, setRequests] = useState(mockRequests)
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [page, setPage] = useState(0)
  const [rowsPerPage, setRowsPerPage] = useState(10)

  function handleCreateRequest(range: {
    inputNumberMin: string
    inputNumberMax: string
  }) {
    const now = new Date().toISOString()
    const newRequest: IPrimeRequest = {
      id: crypto.randomUUID(),
      ...range,
      status: 'PENDING',
      createdAt: now,
      updatedAt: now,
    }

    setRequests((currentRequests) => [newRequest, ...currentRequests])
    setPage(0)
  }

  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <Stack
        sx={{
          mb: 3,
          display: 'flex',
          flexDirection: { xs: 'column', sm: 'row' },
          justifyContent: 'space-between',
          alignItems: { xs: 'stretch', sm: 'center' },
          gap: 2,
        }}
      >
        <Stack spacing={1}>
          <Typography component="h1" variant="h4">
            Requests
          </Typography>
          <Typography color="text.secondary">
            Track your prime number generation requests.
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Demo data: requests are not yet sent to the orchestrator.
          </Typography>
        </Stack>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => setIsCreateDialogOpen(true)}
        >
          New request
        </Button>
      </Stack>
      <Paper variant="outlined">
        <RequestsTable
          requests={requests}
          page={page}
          rowsPerPage={rowsPerPage}
          onPageChange={setPage}
          onRowsPerPageChange={(newRowsPerPage) => {
            setRowsPerPage(newRowsPerPage)
            setPage(0)
          }}
        />
      </Paper>
      <CreateRequestDialog
        open={isCreateDialogOpen}
        onClose={() => setIsCreateDialogOpen(false)}
        onCreate={handleCreateRequest}
      />
    </Container>
  )
}

export default RequestsPage
