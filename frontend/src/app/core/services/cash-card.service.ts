import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { CashCardResponse, CreateCashCardRequest } from '../models/card.model';

@Injectable({
  providedIn: 'root'
})
export class CashCardService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/cards';

  getCards(): Observable<CashCardResponse[]> {
    return this.http.get<CashCardResponse[]>(this.baseUrl);
  }

  getCard(cardId: number): Observable<CashCardResponse> {
    return this.http.get<CashCardResponse>(`${this.baseUrl}/${cardId}`);
  }

  createCard(request: CreateCashCardRequest): Observable<CashCardResponse> {
    return this.http.post<CashCardResponse>(this.baseUrl, request);
  }

  blockCard(cardId: number): Observable<CashCardResponse> {
    return this.http.post<CashCardResponse>(`${this.baseUrl}/${cardId}/block`, {});
  }

  unblockCard(cardId: number): Observable<CashCardResponse> {
    return this.http.post<CashCardResponse>(`${this.baseUrl}/${cardId}/unblock`, {});
  }

  closeCard(cardId: number): Observable<CashCardResponse> {
    return this.http.post<CashCardResponse>(`${this.baseUrl}/${cardId}/close`, {});
  }
}