import type { IPrimeRequest } from '../interfaces/IPrimeRequest'
import type {
  IPrimeRequestDetails,
  IProcessingResultDetails,
} from '../interfaces/IPrimeRequestDetails'

interface IPageableResult<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
}

interface IPrimeRequestApiDetails extends IPrimeRequest {
  results: IProcessingResultDetails | null
}

async function apiRequest<T>(
  path: string,
  idToken: string,
  options: RequestInit = {},
): Promise<T> {
  const headers = new Headers(options.headers)
  headers.set('Authorization', `Bearer ${idToken}`)

  if (options.body) {
    headers.set('Content-Type', 'application/json')
  }

  const response = await fetch(path, {
    ...options,
    headers,
  })

  if (!response.ok) {
    const errorBody: { detail?: string; message?: string } =
      await response.json().catch(() => ({}))
    throw new Error(
      errorBody.detail ??
        errorBody.message ??
        `The request failed with status ${response.status}.`,
    )
  }

  return response.json() as Promise<T>
}

export function getPrimeRequests(
  idToken: string,
  page: number,
  size: number,
  signal?: AbortSignal,
) {
  const query = new URLSearchParams({ page: String(page), size: String(size) })
  return apiRequest<IPageableResult<IPrimeRequest>>(
    `/api/v1/prime-requests?${query}`,
    idToken,
    { signal },
  )
}

export function createPrimeRequest(
  idToken: string,
  range: Pick<IPrimeRequest, 'inputNumberMin' | 'inputNumberMax'>,
) {
  return apiRequest<string>('/api/v1/prime-requests', idToken, {
    method: 'POST',
    body: JSON.stringify(range),
  })
}

export async function getPrimeRequest(
  idToken: string,
  requestId: string,
  signal?: AbortSignal,
): Promise<IPrimeRequestDetails> {
  const request = await apiRequest<IPrimeRequestApiDetails>(
    `/api/v1/prime-requests/${encodeURIComponent(requestId)}`,
    idToken,
    { signal },
  )

  return {
    ...request,
    processingResult: request.results ?? { state: 'IN_QUEUE' },
  }
}

export function getApiErrorMessage(error: unknown) {
  return error instanceof Error ? error.message : 'An unexpected error occurred.'
}
