import { Chip, Link, TableCell, TableRow } from '@mui/material'
import type { ChipProps } from '@mui/material/Chip'
import { Link as RouterLink } from 'react-router'
import type {
  IPrimeRequest,
  PrimeRequestStatus,
} from '../../interfaces/IPrimeRequest'

const statusPresentation = {
  PENDING: { label: 'Pendente', color: 'warning' },
  PROCESSING: { label: 'Em processamento', color: 'info' },
  COMPLETED: { label: 'Concluído', color: 'success' },
} satisfies Record<
  PrimeRequestStatus,
  { label: string; color: ChipProps['color'] }
>

interface RequestsTableRowProps {
  request: IPrimeRequest
}

function RequestsTableRow({ request }: RequestsTableRowProps) {
  const status = statusPresentation[request.status]

  return (
    <TableRow hover>
      <TableCell>
        {request.id}
      </TableCell>
      <TableCell>
        {request.inputNumberMin} a {request.inputNumberMax}
      </TableCell>
      <TableCell>
        <Chip label={status.label} color={status.color} size="small" />
      </TableCell>
      <TableCell>
        {new Date(request.createdAt).toLocaleString('pt-BR')}
      </TableCell>
      <TableCell>
        {new Date(request.updatedAt).toLocaleString('pt-BR')}
      </TableCell>
      <TableCell>
        <Link component={RouterLink} to={`/requests/${request.id}`}>
          Ver detalhes
        </Link>
      </TableCell>
    </TableRow>
  )
}

export default RequestsTableRow
