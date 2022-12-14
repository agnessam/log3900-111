import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { MatDialogRef } from '@angular/material/dialog';
import { MaterialModules } from 'src/app/app-material.module';
import { NewDrawingAlertComponent } from './new-drawing-alert.component';

describe('NewDrawingAlertComponent', () => {
  let component: NewDrawingAlertComponent;
  let fixture: ComponentFixture<NewDrawingAlertComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [MaterialModules],
      declarations: [NewDrawingAlertComponent],
      providers: [{ provide: MatDialogRef, useValue: {} }],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewDrawingAlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create new alert component', () => {
    expect(component).toBeTruthy();
  });
});
