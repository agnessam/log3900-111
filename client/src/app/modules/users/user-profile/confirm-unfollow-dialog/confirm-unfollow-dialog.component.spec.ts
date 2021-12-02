import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmUnfollowDialogComponent } from './confirm-unfollow-dialog.component';

describe('ConfirmUnfollowDialogComponent', () => {
  let component: ConfirmUnfollowDialogComponent;
  let fixture: ComponentFixture<ConfirmUnfollowDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmUnfollowDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmUnfollowDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
