import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalChatComponent } from './external-chat.component';

describe('ExternalChatComponent', () => {
  let component: ExternalChatComponent;
  let fixture: ComponentFixture<ExternalChatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExternalChatComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExternalChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
