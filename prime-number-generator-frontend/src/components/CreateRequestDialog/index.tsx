import { useState } from 'react'
import type { FormEvent } from 'react'
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import type { ICreateRequestDialogProps } from '../../interfaces/ICreateRequestDialogProps'

function CreateRequestDialog({
  open,
  onClose,
  onCreate,
}: ICreateRequestDialogProps) {
  const [inputNumberMin, setInputNumberMin] = useState('')
  const [inputNumberMax, setInputNumberMax] = useState('')
  const [validationError, setValidationError] = useState('')

  function handleClose() {
    setInputNumberMin('')
    setInputNumberMax('')
    setValidationError('')
    onClose()
  }

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (!/^\d+$/.test(inputNumberMin) || !/^\d+$/.test(inputNumberMax)) {
      setValidationError('Informe valores inteiros não negativos.')
      return
    }

    const difference = BigInt(inputNumberMax) - BigInt(inputNumberMin)

    if (difference < 10n || difference > 1000n) {
      setValidationError(
        'O valor máximo deve estar entre 10 e 1.000 unidades acima do mínimo.',
      )
      return
    }

    onCreate({ inputNumberMin, inputNumberMax })
    handleClose()
  }

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      fullWidth
      maxWidth="sm"
      aria-labelledby="create-request-title"
    >
      <form onSubmit={handleSubmit}>
        <DialogTitle id="create-request-title">
          Nova solicitação
        </DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <Typography color="text.secondary">
              Informe o intervalo de números para a geração de primos. O mínimo
              deve ser maior ou igual a zero e a diferença entre máximo e
              mínimo deve ficar entre 10 e 1.000.
            </Typography>
            <TextField
              label="Número mínimo"
              value={inputNumberMin}
              onChange={(event) => {
                setInputNumberMin(event.target.value)
                setValidationError('')
              }}
              slotProps={{
                htmlInput: { inputMode: 'numeric', pattern: '[0-9]*' },
              }}
              required
              fullWidth
            />
            <TextField
              label="Número máximo"
              value={inputNumberMax}
              onChange={(event) => {
                setInputNumberMax(event.target.value)
                setValidationError('')
              }}
              slotProps={{
                htmlInput: { inputMode: 'numeric', pattern: '[0-9]*' },
              }}
              required
              fullWidth
            />
            {validationError && (
              <Typography role="alert" color="error">
                {validationError}
              </Typography>
            )}
          </Stack>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button onClick={handleClose}>Cancelar</Button>
          <Button type="submit" variant="contained">
            Criar solicitação
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  )
}

export default CreateRequestDialog
