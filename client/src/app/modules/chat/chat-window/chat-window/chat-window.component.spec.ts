import { ComponentFixture, TestBed } from '@angular/core/testing';

<<<<<<< HEAD:client/src/app/modules/chat/chat-window/chat-window/chat-window.component.spec.ts
import { ChatWindowComponent } from './chat-window.component';

describe('ChatWindowComponent', () => {
  let component: ChatWindowComponent;
  let fixture: ComponentFixture<ChatWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChatWindowComponent ]
=======
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterComponent ]
>>>>>>> main:client/src/app/modules/authentication/register/register.component.spec.ts
    })
    .compileComponents();
  });

  beforeEach(() => {
<<<<<<< HEAD:client/src/app/modules/chat/chat-window/chat-window/chat-window.component.spec.ts
    fixture = TestBed.createComponent(ChatWindowComponent);
=======
    fixture = TestBed.createComponent(RegisterComponent);
>>>>>>> main:client/src/app/modules/authentication/register/register.component.spec.ts
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
