import { Component, inject, signal } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  form = {
    email: '',
    password: ''
  };

  submit(): void {
    this.error.set(null);
    this.loading.set(true);

    this.authService.login({
      email: this.form.email,
      password: this.form.password
    }).subscribe({
      next: () => {
        this.router.navigate(['/cards']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.error?.message ?? 'Connexion impossible');
      }
    });
  }
}
