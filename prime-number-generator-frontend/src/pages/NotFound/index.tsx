import { Container } from '@mui/material'
import NotFound from '../../components/NotFound'

function NotFoundPage() {
  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <NotFound message="The requested address does not match any page." />
    </Container>
  )
}

export default NotFoundPage
