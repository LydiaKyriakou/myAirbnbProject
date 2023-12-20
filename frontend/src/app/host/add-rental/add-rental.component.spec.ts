import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRentalComponent } from './add-rental.component';

describe('AddRentalComponent', () => {
  let component: AddRentalComponent;
  let fixture: ComponentFixture<AddRentalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddRentalComponent]
    });
    fixture = TestBed.createComponent(AddRentalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
