export type PrimeRequestStatus = 'PENDING' | 'PROCESSING' | 'COMPLETED'

export interface IPrimeRequest {
  id: string
  inputNumberMin: string
  inputNumberMax: string
  status: PrimeRequestStatus
  createdAt: string
  updatedAt: string
}
