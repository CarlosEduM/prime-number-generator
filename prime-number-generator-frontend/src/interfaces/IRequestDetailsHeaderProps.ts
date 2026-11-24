import type { IPrimeRequest } from './IPrimeRequest'
import type { ProcessingResultState } from './IPrimeRequestDetails'

export interface IRequestDetailsHeaderProps {
  request: IPrimeRequest
  resultState: ProcessingResultState
}
