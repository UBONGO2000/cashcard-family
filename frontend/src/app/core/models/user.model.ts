export type UserRole =
  | 'PARENT'
  | 'CHILD'
  | 'ADMIN';

export interface UserResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  createdAt: string;
}