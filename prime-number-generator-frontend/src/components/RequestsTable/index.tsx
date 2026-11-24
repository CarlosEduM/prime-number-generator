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
        <Table aria-label="Solicitações de geração de números primos">
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Intervalo solicitado</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Criada em</TableCell>
              <TableCell>Atualizada em</TableCell>
              <TableCell>Ações</TableCell>
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
                  Nenhum processo encontrado.
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
        labelRowsPerPage="Itens por página:"
        labelDisplayedRows={({ from, to, count }) =>
          `${from}–${to} de ${count !== -1 ? count : `mais de ${to}`}`
        }
      />
    </>
  )
}

export default RequestsTable
