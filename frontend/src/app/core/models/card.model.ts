export type CardStatus =
  | 'ACTIVE'
  | 'BLOCKED'
  | 'CLOSED';

export interface CashCardResponse {
  id: number;
  name: string;
  balance: number;
  spendingLimit: number | null;
  status: CardStatus;
  ownerId: number;
  createdAt: string;
}

export interface CreateCashCardRequest {
  name: string;
  initialBalance: number;
  spendingLimit: number | null;
}

export interface UpdateCashCardRequest {
  name?: string;
  spendingLimit?: number | null;
}