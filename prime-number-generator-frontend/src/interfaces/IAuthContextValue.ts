export interface IAuthContextValue {
  idToken: string | null
  signIn: (idToken: string) => void
}
