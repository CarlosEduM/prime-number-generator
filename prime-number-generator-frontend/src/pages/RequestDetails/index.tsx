import { useEffect, useState } from 'react'
import { Alert, Container, Stack } from '@mui/material'
import { useParams } from 'react-router'
import RequestDetailsHeader from '../../components/RequestDetailsHeader'
import RequestDetailsLoading from '../../components/RequestDetailsLoading'
import NotFound from '../../components/NotFound'
import RequestProgress from '../../components/RequestProgress'
import RequestResult from '../../components/RequestResult'
import RequestSummary from '../../components/RequestSummary'
import type { IPrimeRequestDetails } from '../../interfaces/IPrimeRequestDetails'
import { useAuth } from '../../hooks/useAuth'
import {
  getApiErrorMessage,
  getPrimeRequest,
} from '../../services/primeRequests'

function RequestDetailsPage() {
  const { requestId } = useParams()
  const { idToken } = useAuth()
  const [loadedRequestId, setLoadedRequestId] = useState<string>()
  const [details, setDetails] = useState<IPrimeRequestDetails>()
  const [error, setError] = useState('')

  useEffect(() => {
    if (!requestId || !idToken) {
      return
    }

    const id = requestId
    const token = idToken
    let isActive = true
    const controller = new AbortController()

    async function loadRequest() {
      try {
        const request = await getPrimeRequest(
          token,
          id,
          controller.signal,
        )
        if (isActive) {
          setDetails(request)
          setLoadedRequestId(id)
          setError('')
        }
      } catch (requestError) {
        if (isActive && !controller.signal.aborted) {
          setError(getApiErrorMessage(requestError))
          setLoadedRequestId(id)
        }
      }
    }

    void loadRequest()
    const intervalId = window.setInterval(() => {
      void loadRequest()
    }, 5_000)

    return () => {
      isActive = false
      controller.abort()
      window.clearInterval(intervalId)
    }
  }, [idToken, requestId])

  const isLoading = loadedRequestId !== requestId
  const currentDetails =
    details?.id === requestId ? details : undefined

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      {isLoading ? (
        <RequestDetailsLoading />
      ) : error && !currentDetails ? (
        <Alert severity="error">Could not load request: {error}</Alert>
      ) : !currentDetails ? (
        <NotFound
          message={`The request with ID "${requestId}" was not found.`}
        />
      ) : (
        <Stack spacing={3}>
          {error && (
            <Alert severity="error">
              Could not refresh request details: {error}
            </Alert>
          )}
          <RequestDetailsHeader
            request={currentDetails}
            resultState={currentDetails.processingResult.state}
          />
          <RequestSummary request={currentDetails} />
          <RequestProgress status={currentDetails.status} />
          <RequestResult
            processingResult={currentDetails.processingResult}
          />
        </Stack>
      )}
    </Container>
  )
}

export default RequestDetailsPage
