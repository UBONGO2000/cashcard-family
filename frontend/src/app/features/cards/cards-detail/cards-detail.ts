import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CashCardService } from '../../../core/services/cash-card.service';
import { TransactionService } from '../../../core/services/ transaction.service';
import { CashCardResponse } from '../../../core/models/card.model';
import { CardTransactionResponse } from '../../../core/models/transaction.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cards-detail',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cards-detail.html',
  styleUrl: './cards-detail.scss',
})
export class CardsDetail implements OnInit{
    private readonly route = inject(ActivatedRoute);
  private readonly cashCardService = inject(CashCardService);
  private readonly transactionService = inject(TransactionService);

  readonly card = signal<CashCardResponse | null>(null);
  readonly transactions = signal<CardTransactionResponse[]>([]);
  readonly loading = signal(false);
  readonly transactionsLoading = signal(false);
  readonly error = signal<string | null>(null);

  cardId!: number;

  depositForm = {
    amount: '',
    reason: ''
  };

  paymentForm = {
    amount: '',
    merchant: ''
  };

  ngOnInit(): void {
    const paramCardId = this.route.snapshot.paramMap.get('cardId');

    if (!paramCardId) {
      this.error.set('Carte non trouvée.');
      return;
    }

    this.cardId = Number(paramCardId);
    this.loadCard();
    this.loadTransactions();
  }

  loadCard(): void {
    this.loading.set(true);
    this.error.set(null);

    this.cashCardService.getCard(this.cardId).subscribe({
      next: (card) => {
        this.card.set(card);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message ?? 'Impossible de charger la carte.');
        this.loading.set(false);
      }
    });
  }

  loadTransactions(): void {
    this.transactionsLoading.set(true);

    this.transactionService.getTransactions(this.cardId).subscribe({
      next: (page) => {
        this.transactions.set(page.content);
        this.transactionsLoading.set(false);
      },
      error: () => {
        this.transactions.set([]);
        this.transactionsLoading.set(false);
      }
    });
  }

  deposit(): void {
    const request = {
      amount: Number(this.depositForm.amount),
      reason: this.depositForm.reason || null
    };

    this.transactionService.deposit(this.cardId, request).subscribe({
      next: (response) => {
        this.card.set(response.card);
        this.depositForm = { amount: '', reason: '' };
        this.loadTransactions();
      },
      error: (err) => {
        this.error.set(err.error?.message ?? 'Impossible de faire le dépôt.');
      }
    });
  }

  pay(): void {
    const request = {
      amount: Number(this.paymentForm.amount),
      merchant: this.paymentForm.merchant
    };

    this.transactionService.pay(this.cardId, request).subscribe({
      next: (response) => {
        this.card.set(response.card);
        this.paymentForm = { amount: '', merchant: '' };
        this.loadTransactions();
      },
      error: (err) => {
        this.error.set(err.error?.message ?? 'Impossible de faire le paiement.');
      }
    });
  }

  blockCard(): void {
    this.cashCardService.blockCard(this.cardId).subscribe({
      next: (card) => this.card.set(card),
      error: (err) =>
        this.error.set(err.error?.message ?? 'Impossible de bloquer la carte.')
    });
  }

  unblockCard(): void {
    this.cashCardService.unblockCard(this.cardId).subscribe({
      next: (card) => this.card.set(card),
      error: (err) =>
        this.error.set(err.error?.message ?? 'Impossible de débloquer la carte.')
    });
  }

  closeCard(): void {
    this.cashCardService.closeCard(this.cardId).subscribe({
      next: (card) => this.card.set(card),
      error: (err) =>
        this.error.set(err.error?.message ?? 'Impossible de fermer la carte.')
    });
  }

  statusLabel(status: string): string {
  switch (status) {
    case 'ACTIVE':
      return 'Active';
    case 'BLOCKED':
      return 'Bloquée';
    case 'CLOSED':
      return 'Fermée';
    default:
      return status;
  }
}

statusClass(status: string): string {
  switch (status) {
    case 'ACTIVE':
      return 'status-active';
    case 'BLOCKED':
      return 'status-blocked';
    case 'CLOSED':
      return 'status-closed';
    default:
      return '';
  }
}
}
