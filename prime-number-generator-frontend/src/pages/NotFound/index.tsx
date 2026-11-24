import { Container } from '@mui/material'
import NotFound from '../../components/NotFound'

function NotFoundPage() {
  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <NotFound message="O endereço acessado não corresponde a nenhuma página." />
    </Container>
  )
}

export default NotFoundPage
