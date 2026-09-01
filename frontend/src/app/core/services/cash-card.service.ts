import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  CashCardResponse,
  CreateCashCardRequest,
  UpdateCashCardRequest
} from '../models/card.model';

@Injectable({
  providedIn: 'root'
})
export class CashCardService {
  private readonly http = inject(HttpClient);

  // TODO : à remplacer par l'utilisateur authentifié avec Spring Security plus tard
  private readonly currentUserId = 2;

  private readonly baseUrl = `/api/users/${this.currentUserId}/cards`;

  getCards(): Observable<CashCardResponse[]> {
    return this.http.get<CashCardResponse[]>(this.baseUrl);
  }

  getCard(cardId: number): Observable<CashCardResponse> {
    return this.http.get<CashCardResponse>(`${this.baseUrl}/${cardId}`);
  }

  createCard(request: CreateCashCardRequest): Observable<CashCardResponse> {
    return this.http.post<CashCardResponse>(this.baseUrl, request);
  }

  updateCard(cardId: number, request: UpdateCashCardRequest): Observable<CashCardResponse> {
    return this.http.patch<CashCardResponse>(`${this.baseUrl}/${cardId}`, request);
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