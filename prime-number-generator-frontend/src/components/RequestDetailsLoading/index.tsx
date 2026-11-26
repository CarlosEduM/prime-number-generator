import { Skeleton, Stack } from '@mui/material'

function RequestDetailsLoading() {
  return (
    <Stack
      spacing={2}
      role="status"
      aria-label="Loading request details"
    >
      <Skeleton variant="text" width={240} height={48} />
      <Skeleton variant="rounded" height={130} />
      <Skeleton variant="rounded" height={180} />
      <Skeleton variant="rounded" height={220} />
    </Stack>
  )
}

export default RequestDetailsLoading
