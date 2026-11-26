import type { IPrimeRequest } from './IPrimeRequest'

export interface ICreateRequestDialogProps {
  open: boolean
  onClose: () => void
  onCreate: (
    range: Pick<IPrimeRequest, 'inputNumberMin' | 'inputNumberMax'>,
  ) => Promise<void>
}
