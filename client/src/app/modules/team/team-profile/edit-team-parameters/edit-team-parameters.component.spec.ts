import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTeamParametersComponent } from './edit-team-parameters.component';

describe('EditTeamParametersComponent', () => {
  let component: EditTeamParametersComponent;
  let fixture: ComponentFixture<EditTeamParametersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditTeamParametersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditTeamParametersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
