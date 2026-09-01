import { Routes } from '@angular/router';


export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'cards'
  },
  {
    path: 'cards',
    loadComponent: () =>
      import('./features/cards/cards-list/cards-list')
        .then(m => m.CardsList)
  },
  {
    path: 'cards/:cardId',
    loadComponent: () =>
      import('./features/cards/cards-detail/cards-detail')
        .then(m => m.CardsDetail)
  }
];