import type { IPrimeRequest } from './IPrimeRequest'

export interface IRequestsTableProps {
  requests: IPrimeRequest[]
  page: number
  rowsPerPage: number
  onPageChange: (page: number) => void
  onRowsPerPageChange: (rowsPerPage: number) => void
}
