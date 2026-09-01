import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CashCardService } from '../../../core/services/cash-card.service';
import { CashCardResponse } from '../../../core/models/card.model';

@Component({
  selector: 'app-cards-list',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cards-list.html',
  styleUrl: './cards-list.scss',
})
export class CardsList implements OnInit{
private readonly cashCardService = inject(CashCardService);

  readonly cards = signal<CashCardResponse[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly showCreateForm = signal(false);

  newCard = {
    name: '',
    initialBalance: '',
    spendingLimit: ''
  };

  ngOnInit(): void {
    this.loadCards();
  }

  openCreateForm(): void {
    this.showCreateForm.set(true);
  }

  closeCreateForm(): void {
    this.showCreateForm.set(false);
  }

  loadCards(): void {
    this.loading.set(true);
    this.error.set(null);

    this.cashCardService.getCards().subscribe({
      next: (cards) => {
        this.cards.set(cards);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger les cartes.');
        this.loading.set(false);
      }
    });
  }

  createCard(): void {
    this.error.set(null);

    const request = {
      name: this.newCard.name,
      initialBalance: Number(this.newCard.initialBalance || 0),
      spendingLimit: this.newCard.spendingLimit
        ? Number(this.newCard.spendingLimit)
        : null
    };

    this.cashCardService.createCard(request).subscribe({
      next: () => {
        this.newCard = {
          name: '',
          initialBalance: '',
          spendingLimit: ''
        };

        this.showCreateForm.set(false);
        this.loadCards();
      },
      error: (err) => {
        this.error.set(err.error?.message ?? 'Impossible de créer la carte.');
      }
  });
}
}
