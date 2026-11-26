import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
} from '@mui/material'
import type { IRequestsTableProps } from '../../interfaces/IRequestsTableProps'
import RequestsTableRow from '../RequestsTableRow'

function RequestsTable({
  requests,
  page,
  rowsPerPage,
  onPageChange,
  onRowsPerPageChange,
}: IRequestsTableProps) {
  return (
    <>
      <TableContainer sx={{ overflowX: 'auto' }}>
        <Table aria-label="Prime number generation requests">
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Requested range</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Created at</TableCell>
              <TableCell>Updated at</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {requests
              .slice(
                page * rowsPerPage,
                page * rowsPerPage + rowsPerPage,
              )
              .map((request) => (
                <RequestsTableRow key={request.id} request={request} />
              ))}
            {requests.length === 0 && (
              <TableRow>
                <TableCell colSpan={6} align="center">
                  No requests found.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        component="div"
        count={requests.length}
        page={page}
        onPageChange={(_, newPage) => onPageChange(newPage)}
        rowsPerPage={rowsPerPage}
        onRowsPerPageChange={(event) => {
          onRowsPerPageChange(Number(event.target.value))
        }}
        rowsPerPageOptions={[5, 10, 25]}
        labelRowsPerPage="Items per page:"
        labelDisplayedRows={({ from, to, count }) =>
          `${from}–${to} of ${count !== -1 ? count : `more than ${to}`}`
        }
      />
    </>
  )
}

export default RequestsTable
