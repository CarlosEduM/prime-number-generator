import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material'
import type { IPrimeRequest } from '../../interfaces/IPrimeRequest'
import RequestsTableRow from '../RequestsTableRow'

interface RequestsTableProps {
  requests: IPrimeRequest[]
}

function RequestsTable({ requests }: RequestsTableProps) {
  return (
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
          {requests.map((request) => (
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
  )
}

export default RequestsTable
