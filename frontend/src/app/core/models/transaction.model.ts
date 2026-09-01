import { CashCardResponse } from './card.model';

export type TransactionType =
  | 'DEPOSIT'
  | 'PAYMENT'
  | 'REFUND';

export type TransactionStatus =
  | 'COMPLETED'
  | 'REJECTED'
  | 'CANCELLED';

export interface CardTransactionResponse {
  id: number;
  cardId: number;
  type: TransactionType;
  status: TransactionStatus;
  amount: number;
  balanceAfter: number;
  merchant: string | null;
  reason: string | null;
  createdAt: string;
}

export interface DepositRequest {
  amount: number;
  reason: string | null;
}

export interface PaymentRequest {
  amount: number;
  merchant: string;
}

export interface RefundRequest {
  amount: number;
  reason: string | null;
}

export interface OperationResponse {
  card: CashCardResponse;
  transaction: CardTransactionResponse;
}