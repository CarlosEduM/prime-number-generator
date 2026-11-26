import type { IPrimeRequest } from './IPrimeRequest'

export interface IRequestsTableProps {
  requests: IPrimeRequest[]
  totalElements: number
  page: number
  rowsPerPage: number
  onPageChange: (page: number) => void
  onRowsPerPageChange: (rowsPerPage: number) => void
}
