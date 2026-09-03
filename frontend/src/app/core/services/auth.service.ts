import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { TokenStorageService } from './token-storage.service';
import { UserResponse } from '../models/user.model';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface AuthResponse {
  token: string;
  user: UserResponse;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenStorage = inject(TokenStorageService);
  private readonly router = inject(Router);

  // Signal pour l'utilisateur connecté, accessible depuis toute l'app
  readonly currentUser = signal<UserResponse | null>(this.tokenStorage.getUser<UserResponse>());

  private readonly baseUrl = '/api/auth';

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, request).pipe(
      tap((response) => {
        this.tokenStorage.saveToken(response.token);
        this.tokenStorage.saveUser(response.user);
        this.currentUser.set(response.user);
      })
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, request).pipe(
      tap((response) => {
        this.tokenStorage.saveToken(response.token);
        this.tokenStorage.saveUser(response.user);
        this.currentUser.set(response.user);
      })
    );
  }

  logout(): void {
    this.tokenStorage.clear();
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.tokenStorage.isAuthenticated();
  }

  getCurrentUserId(): number | null {
    return this.currentUser()?.id ?? null;
  }
}