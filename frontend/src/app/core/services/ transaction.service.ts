import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { SpringPage } from '../models/page.model';
import {
  CardTransactionResponse,
  DepositRequest,
  OperationResponse,
  PaymentRequest,
  RefundRequest
} from '../models/transaction.model';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/cards';

  getTransactions(
    cardId: number,
    page = 0,
    size = 20
  ): Observable<SpringPage<CardTransactionResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<SpringPage<CardTransactionResponse>>(
      `${this.baseUrl}/${cardId}/transactions`,
      { params }
    );
  }

  deposit(cardId: number, request: DepositRequest): Observable<OperationResponse> {
    return this.http.post<OperationResponse>(
      `${this.baseUrl}/${cardId}/deposits`,
      request
    );
  }

  pay(cardId: number, request: PaymentRequest): Observable<OperationResponse> {
    return this.http.post<OperationResponse>(
      `${this.baseUrl}/${cardId}/payments`,
      request
    );
  }

  refund(cardId: number, request: RefundRequest): Observable<OperationResponse> {
    return this.http.post<OperationResponse>(
      `${this.baseUrl}/${cardId}/refunds`,
      request
    );
  }
}