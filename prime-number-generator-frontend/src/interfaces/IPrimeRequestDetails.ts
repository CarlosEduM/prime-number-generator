import type { IPrimeRequest } from './IPrimeRequest'

export type ProcessingResultState =
  | 'IN_QUEUE'
  | 'PROCESSING'
  | 'SUCCESS'
  | 'FAIL'

export interface IProcessingResultDetails {
  state: ProcessingResultState
  results?: string[]
  errorMessage?: string
  startedAt?: string
  finishedAt?: string
}

export interface IPrimeRequestDetails extends IPrimeRequest {
  processingResult: IProcessingResultDetails
}
