import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ToolsApplierColorsService } from "src/app/modules/workspace/services/tools/tools-applier-colors/tools-applier-colors.service"
import { ApplierToolParameterComponent } from './applier-tool-parameter.component';

describe('ApplierToolParameterComponent', () => {
  let component: ApplierToolParameterComponent;
  let fixture: ComponentFixture<ApplierToolParameterComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ApplierToolParameterComponent ],
      providers: [
        {
          provide: ToolsApplierColorsService, useClass: class {
            toolName = 'ToolTest';
        }}],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplierToolParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should get tool name', () => {
    expect(component.toolName).toEqual('ToolTest');
  });
});
