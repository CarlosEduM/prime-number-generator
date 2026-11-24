import { useState } from 'react'
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
} from '@mui/material'
import type { IPrimeRequest } from '../../interfaces/IPrimeRequest'
import RequestsTableRow from '../RequestsTableRow'

interface RequestsTableProps {
  requests: IPrimeRequest[]
}

function RequestsTable({ requests }: RequestsTableProps) {
  const [page, setPage] = useState(0)
  const [rowsPerPage, setRowsPerPage] = useState(10)
  const pageCount = Math.ceil(requests.length / rowsPerPage)
  const currentPage = Math.min(page, Math.max(0, pageCount - 1))

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
                currentPage * rowsPerPage,
                currentPage * rowsPerPage + rowsPerPage,
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
        page={currentPage}
        onPageChange={(_, newPage) => setPage(newPage)}
        rowsPerPage={rowsPerPage}
        onRowsPerPageChange={(event) => {
          setRowsPerPage(Number(event.target.value))
          setPage(0)
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
