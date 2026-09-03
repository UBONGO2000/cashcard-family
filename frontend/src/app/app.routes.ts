import { Routes } from '@angular/router';


import { authGuard, guestGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'cards'
  },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/login/login').then(m => m.Login)
  },
  {
    path: 'register',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/register/register').then(m => m.Register)
  },
  {
    path: 'cards',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/cards/cards-list/cards-list').then(m => m.CardsList)
  },
  {
    path: 'cards/:cardId',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/cards/cards-detail/cards-detail').then(m => m.CardsDetail)
  }
];