import { Component, inject, signal } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  form = {
    email: '',
    password: '',
    firstName: '',
    lastName: ''
  };

  submit(): void {
    this.error.set(null);
    this.loading.set(true);

    this.authService.register(this.form).subscribe({
      next: () => {
        this.router.navigate(['/cards']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.error?.message ?? 'Inscription impossible');
      }
    });
  }
}