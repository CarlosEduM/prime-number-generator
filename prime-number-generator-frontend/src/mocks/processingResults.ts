import type { IPrimeRequest } from '../interfaces/IPrimeRequest'
import type { IProcessingResultDetails } from '../interfaces/IPrimeRequestDetails'

const failedRequestId = '2a54d46e-ffdc-4df8-9968-f0c93a137542'

const mockPrimeNumbers = [
  '2',
  '3',
  '5',
  '7',
  '11',
  '13',
  '17',
  '19',
  '23',
  '29',
  '31',
  '37',
  '41',
  '43',
  '47',
  '53',
  '59',
  '61',
  '67',
  '71',
  '73',
  '79',
  '83',
  '89',
  '97',
]

export function getMockProcessingResult(
  request: IPrimeRequest,
): IProcessingResultDetails {
  if (request.status === 'PENDING') {
    return { state: 'IN_QUEUE' }
  }

  if (request.status === 'PROCESSING') {
    return {
      state: 'PROCESSING',
      startedAt: request.updatedAt,
    }
  }

  if (request.id === failedRequestId) {
    return {
      state: 'FAIL',
      errorMessage:
        'The processor could not complete this request. Please try again later.',
      finishedAt: request.updatedAt,
    }
  }

  return {
    state: 'SUCCESS',
    results: mockPrimeNumbers,
    startedAt: request.createdAt,
    finishedAt: request.updatedAt,
  }
}
