import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardsDetail } from './cards-detail';

describe('CardsDetail', () => {
  let component: CardsDetail;
  let fixture: ComponentFixture<CardsDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardsDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(CardsDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
