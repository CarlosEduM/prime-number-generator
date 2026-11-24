import { createContext } from 'react'
import type { IAuthContextValue } from '../interfaces/IAuthContextValue'

export const AuthContext = createContext<IAuthContextValue | null>(null)
