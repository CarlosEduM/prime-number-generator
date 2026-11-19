import { Container, Typography } from '@mui/material'
import { useParams } from 'react-router'

function RequestDetailsPage() {
  const { requestId } = useParams()

  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <Typography component="h1" variant="h4">
        Detalhes da solicitação {requestId}
      </Typography>
    </Container>
  )
}

export default RequestDetailsPage
