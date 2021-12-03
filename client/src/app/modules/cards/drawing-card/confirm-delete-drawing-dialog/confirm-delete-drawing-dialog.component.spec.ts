import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDeleteDrawingDialogComponent } from './confirm-delete-drawing-dialog.component';

describe('ConfirmDeleteDrawingDialogComponent', () => {
  let component: ConfirmDeleteDrawingDialogComponent;
  let fixture: ComponentFixture<ConfirmDeleteDrawingDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmDeleteDrawingDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmDeleteDrawingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
