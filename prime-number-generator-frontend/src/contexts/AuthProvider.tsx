import { useState } from 'react'
import type { PropsWithChildren } from 'react'
import { AuthContext } from './AuthContext'

export function AuthProvider({ children }: PropsWithChildren) {
  const [idToken, setIdToken] = useState<string | null>(null)

  return (
    <AuthContext.Provider value={{ idToken, signIn: setIdToken }}>
      {children}
    </AuthContext.Provider>
  )
}
