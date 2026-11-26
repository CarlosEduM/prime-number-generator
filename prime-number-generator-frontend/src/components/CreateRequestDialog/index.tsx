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
  const [requestError, setRequestError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  function handleClose() {
    setInputNumberMin('')
    setInputNumberMax('')
    setValidationError('')
    setRequestError('')
    onClose()
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (!/^\d+$/.test(inputNumberMin) || !/^\d+$/.test(inputNumberMax)) {
      setValidationError('Enter non-negative integer values.')
      return
    }

    const difference = BigInt(inputNumberMax) - BigInt(inputNumberMin)

    if (difference < 10n || difference > 1000n) {
      setValidationError(
        'The maximum value must be between 10 and 1,000 units above the minimum.',
      )
      return
    }

    setIsSubmitting(true)
    setRequestError('')
    try {
      await onCreate({ inputNumberMin, inputNumberMax })
      handleClose()
    } catch (error) {
      setRequestError(
        error instanceof Error ? error.message : 'Could not create the request.',
      )
    } finally {
      setIsSubmitting(false)
    }
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
          New request
        </DialogTitle>
        <DialogContent>
          <Stack spacing={2} sx={{ pt: 1 }}>
            <Typography color="text.secondary">
              Enter the number range for prime generation. The minimum must be
              greater than or equal to zero, and the difference between maximum
              and minimum must be between 10 and 1,000.
            </Typography>
            <TextField
              label="Minimum number"
              value={inputNumberMin}
              onChange={(event) => {
                setInputNumberMin(event.target.value)
                setValidationError('')
                setRequestError('')
              }}
              slotProps={{
                htmlInput: { inputMode: 'numeric', pattern: '[0-9]*' },
              }}
              required
              fullWidth
            />
            <TextField
              label="Maximum number"
              value={inputNumberMax}
              onChange={(event) => {
                setInputNumberMax(event.target.value)
                setValidationError('')
                setRequestError('')
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
            {requestError && (
              <Typography role="alert" color="error">
                {requestError}
              </Typography>
            )}
          </Stack>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button onClick={handleClose} disabled={isSubmitting}>
            Cancel
          </Button>
          <Button type="submit" variant="contained" disabled={isSubmitting}>
            {isSubmitting ? 'Creating…' : 'Create request'}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  )
}

export default CreateRequestDialog
