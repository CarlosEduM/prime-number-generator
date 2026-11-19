import { Container, Typography } from '@mui/material'

function NotFoundPage() {
  return (
    <Container maxWidth="lg" sx={{ py: 5 }}>
      <Typography component="h1" variant="h4">
        Página não encontrada
      </Typography>
    </Container>
  )
}

export default NotFoundPage
