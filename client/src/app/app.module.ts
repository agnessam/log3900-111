import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MomentModule } from "ngx-moment";
import { MaterialModules } from "./app-material.module";
import { AppRoutingModule } from "./app-routing.module";
import { ColorPickerModule } from "./color-picker/color-picker.module";
import { AlertMessageComponent } from "./components/alert-message/alert-message.component";
import { AppComponent } from "./components/app/app.component";
import { AuthenticationModule } from "./authentication/authentication.module";
import { CanvasComponent } from "./components/canvas/canvas.component";
import { ControlMenuComponent } from "./components/control-menu/control-menu.component";
import { ErrorMessageComponent } from "./components/error-message/error-message.component";
import { HomeComponent } from "./components/home/home.component";
import { NavbarComponent } from "./components/navbar/navbar.component";
import { ChatComponent } from "./components/chat/chat.component";
import { NewDrawingAlertComponent } from "./components/new-drawing/new-drawing-alert/new-drawing-alert.component";
import { NewDrawingFormComponent } from "./components/new-drawing/new-drawing-form/new-drawing-form.component";
import { NewDrawingComponent } from "./components/new-drawing/new-drawing.component";
import { ParameterMenuComponent } from "./components/parameter-menu/parameter-menu.component";
import { ParameterDirective } from "./components/parameter-menu/parameter.directive";
import { SidenavComponent } from "./components/sidenav/sidenav.component";
import { ToolParameterModule } from "./components/tool-parameters/tool-parameter.module";
import { ToolsColorPickerComponent } from "./components/tools-color-picker/tools-color-picker.component";
import { ToolsColorComponent } from "./components/tools-color/tools-color.component";
import { WorkspaceComponent } from "./components/workspace/workspace.component";
import { TestDatabaseComponent } from './components/test-database/test-database.component';

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
    HomeComponent,
    NavbarComponent,
    ChatComponent,
    TestDatabaseComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MaterialModules,
    ColorPickerModule,
    FontAwesomeModule,
    ToolParameterModule,
    MomentModule,
    AuthenticationModule,
    AppRoutingModule,
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
    TestDatabaseComponent,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
