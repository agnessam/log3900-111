import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPrivacyComponent } from './user-privacy.component';

describe('UserPrivacyComponent', () => {
  let component: UserPrivacyComponent;
  let fixture: ComponentFixture<UserPrivacyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserPrivacyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserPrivacyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
