import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MomentModule } from "ngx-moment";
import { AlertMessageComponent } from "./components/alert-message/alert-message.component";
import { MaterialModules } from "./app-material.module";
import { ColorPickerModule } from "./color-picker/color-picker.module";
import { AppComponent } from "./components/app/app.component";
import { CanvasComponent } from "./components/canvas/canvas.component";
import { ControlMenuComponent } from "./components/control-menu/control-menu.component";
import { NewDrawingAlertComponent } from "./components/new-drawing/new-drawing-alert/new-drawing-alert.component";
import { NewDrawingFormComponent } from "./components/new-drawing/new-drawing-form/new-drawing-form.component";
import { NewDrawingComponent } from "./components/new-drawing/new-drawing.component";
import { ParameterMenuComponent } from "./components/parameter-menu/parameter-menu.component";
import { ParameterDirective } from "./components/parameter-menu/parameter.directive";
import { SidenavComponent } from "./components/sidenav/sidenav.component";
import { ToolsColorPickerComponent } from "./components/tools-color-picker/tools-color-picker.component";
import { ToolsColorComponent } from "./components/tools-color/tools-color.component";
import { WelcomeDialogModule } from "./components/welcome-dialog/welcome-dialog.module";
import { WorkspaceComponent } from "./components/workspace/workspace.component";
import { ErrorMessageComponent } from "./components/error-message/error-message.component";
import { ToolParameterModule } from "./components/tool-parameters/tool-parameter.module";

@NgModule({
  declarations: [
    AppComponent,
    ParameterMenuComponent,
    WorkspaceComponent,
    SidenavComponent,
    CanvasComponent,
    NewDrawingComponent,
    NewDrawingFormComponent,
    NewDrawingAlertComponent,
    ToolsColorComponent,
    ToolsColorPickerComponent,
    ParameterMenuComponent,
    WorkspaceComponent,
    SidenavComponent,
    CanvasComponent,
    ControlMenuComponent,
    ParameterDirective,
    ErrorMessageComponent,
    AlertMessageComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MaterialModules,
    WelcomeDialogModule,
    ColorPickerModule,
    FontAwesomeModule,
    ToolParameterModule,
    MomentModule,
  ],
  exports: [],
  entryComponents: [
    NewDrawingAlertComponent,
    NewDrawingComponent,
    ToolsColorPickerComponent,
    WorkspaceComponent,
    ControlMenuComponent,
    ErrorMessageComponent,
    AlertMessageComponent,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
