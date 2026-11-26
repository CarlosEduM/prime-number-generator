import { useEffect, useState } from 'react'
import { Container, Stack, Typography } from '@mui/material'
import { useLocation, useParams } from 'react-router'
import RequestDetailsHeader from '../../components/RequestDetailsHeader'
import RequestDetailsLoading from '../../components/RequestDetailsLoading'
import NotFound from '../../components/NotFound'
import RequestProgress from '../../components/RequestProgress'
import RequestResult from '../../components/RequestResult'
import RequestSummary from '../../components/RequestSummary'
import type { IPrimeRequestDetails } from '../../interfaces/IPrimeRequestDetails'
import type { IRequestDetailsRouteState } from '../../interfaces/IRequestDetailsRouteState'
import { mockRequests } from '../../mocks/primeRequests'
import { getMockProcessingResult } from '../../mocks/processingResults'

function RequestDetailsPage() {
  const { requestId } = useParams()
  const location = useLocation()
  const [loadedRequestId, setLoadedRequestId] = useState<string>()

  useEffect(() => {
    const timeoutId = window.setTimeout(
      () => setLoadedRequestId(requestId),
      250,
    )
    return () => window.clearTimeout(timeoutId)
  }, [requestId])

  const isLoading = loadedRequestId !== requestId
  const routeRequest = (location.state as IRequestDetailsRouteState | null)
    ?.request
  const request =
    routeRequest?.id === requestId
      ? routeRequest
      : mockRequests.find((item) => item.id === requestId)

  const details: IPrimeRequestDetails | undefined = request
    ? { ...request, processingResult: getMockProcessingResult(request) }
    : undefined

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      {isLoading ? (
        <RequestDetailsLoading />
      ) : !details ? (
        <NotFound
          message={`The request with ID "${requestId}" was not found.`}
        />
      ) : (
        <Stack spacing={3}>
          <RequestDetailsHeader
            request={details}
            resultState={details.processingResult.state}
          />
          <RequestSummary request={details} />
          <RequestProgress status={details.status} />
          <RequestResult processingResult={details.processingResult} />
          <Typography variant="caption" color="text.secondary">
            Demonstration prototype; automatic updates and orchestrator
            integration will be added later.
          </Typography>
        </Stack>
      )}
    </Container>
  )
}

export default RequestDetailsPage
