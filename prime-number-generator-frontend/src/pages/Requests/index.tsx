import { useEffect, useState } from 'react'
import {
  Alert,
  Button,
  Container,
  CircularProgress,
  Paper,
  Stack,
  Typography,
} from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import CreateRequestDialog from '../../components/CreateRequestDialog'
import RequestsTable from '../../components/RequestsTable'
import type { IPrimeRequest } from '../../interfaces/IPrimeRequest'
import { useAuth } from '../../hooks/useAuth'
import {
  createPrimeRequest,
  getApiErrorMessage,
  getPrimeRequests,
} from '../../services/primeRequests'

function RequestsPage() {
  const { idToken } = useAuth()
  const [requests, setRequests] = useState<IPrimeRequest[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [page, setPage] = useState(0)
  const [rowsPerPage, setRowsPerPage] = useState(10)
  const [refreshKey, setRefreshKey] = useState(0)

  useEffect(() => {
    if (!idToken) {
      return
    }

    const token = idToken
    let isActive = true
    const controller = new AbortController()

    async function loadRequests() {
      try {
        const result = await getPrimeRequests(
          token,
          page,
          rowsPerPage,
          controller.signal,
        )
        if (isActive) {
          setRequests(result.content)
          setTotalElements(result.totalElements)
          setError('')
        }
      } catch (requestError) {
        if (isActive && !controller.signal.aborted) {
          setError(getApiErrorMessage(requestError))
        }
      } finally {
        if (isActive) {
          setIsLoading(false)
        }
      }
    }

    void loadRequests()
    const intervalId = window.setInterval(() => {
      void loadRequests()
    }, 10_000)

    return () => {
      isActive = false
      controller.abort()
      window.clearInterval(intervalId)
    }
  }, [idToken, page, rowsPerPage, refreshKey])

  async function handleCreateRequest(range: {
    inputNumberMin: string
    inputNumberMax: string
  }) {
    if (!idToken) {
      throw new Error('Sign in before creating a request.')
    }

    await createPrimeRequest(idToken, range)
    setPage(0)
    setRefreshKey((key) => key + 1)
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
        </Stack>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => setIsCreateDialogOpen(true)}
        >
          New request
        </Button>
      </Stack>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          Could not load requests: {error}
        </Alert>
      )}
      {isLoading && requests.length === 0 && (
        <Stack sx={{ alignItems: 'center', py: 4 }}>
          <CircularProgress aria-label="Loading requests" />
        </Stack>
      )}
      <Paper variant="outlined">
        <RequestsTable
          requests={requests}
          totalElements={totalElements}
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
