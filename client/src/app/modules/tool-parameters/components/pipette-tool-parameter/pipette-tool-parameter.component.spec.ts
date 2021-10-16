import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { PipetteToolService } from 'src/app/modules/workspace';
import { PipetteToolParameterComponent } from 'src/app/modules/tool-parameters';

describe('PipetteToolParameterComponent', () => {
  let component: PipetteToolParameterComponent;
  let fixture: ComponentFixture<PipetteToolParameterComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ PipetteToolParameterComponent ],
      providers: [
        {
          provide: PipetteToolService, useClass: class {
            toolName = 'ToolTest';
          },
        }],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PipetteToolParameterComponent);
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
