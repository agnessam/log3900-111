import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { ToolRectangleService } from 'src/app/modules/workspace';
import { RectangleToolParameterComponent } from 'src/app/modules/tool-parameters';

describe('RectangleToolParameterComponent', () => {
  let component: RectangleToolParameterComponent;
  let fixture: ComponentFixture<RectangleToolParameterComponent>;
  let rectangleToolService: ToolRectangleService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [RectangleToolParameterComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [ReactiveFormsModule,
        MatButtonToggleModule, ],
    })
      .compileComponents();
    rectangleToolService = TestBed.get(ToolRectangleService);

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RectangleToolParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should patch rect value in form', () => {
    component.form = new FormGroup({ rectStyle: new FormControl('fill') });
    const spy = spyOn(component.form, 'patchValue');
    component.selectStyle(1);
    expect(spy).toHaveBeenCalled();
  });

  it('should return the tool name', () => {
    expect(component.toolName).toEqual(rectangleToolService.toolName);
  });
});
