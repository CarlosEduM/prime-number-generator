import { Paper, Step, StepLabel, Stepper, Typography } from '@mui/material'
import type { IRequestProgressProps } from '../../interfaces/IRequestProgressProps'

const steps = ['Na fila', 'Em processamento', 'Concluída']

function RequestProgress({ status }: IRequestProgressProps) {
  const activeStep =
    status === 'PENDING' ? 0 : status === 'PROCESSING' ? 1 : 2

  return (
    <Paper variant="outlined" sx={{ p: 3 }}>
      <Typography component="h2" variant="h6" sx={{ mb: 3 }}>
        Andamento
      </Typography>
      <Stepper activeStep={activeStep} alternativeLabel>
        {steps.map((step) => (
          <Step key={step}>
            <StepLabel>{step}</StepLabel>
          </Step>
        ))}
      </Stepper>
    </Paper>
  )
}

export default RequestProgress
