import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MomentModule } from "ngx-moment";
import { MaterialModules } from "./app-material.module";
import { AppRoutingModule } from "./app-routing.module";
import { WorkspaceModule } from "./modules/workspace/workspace.module";

import { ColorPickerModule } from "src/app/color-picker/color-picker.module";
import { AlertMessageComponent } from "src/app/components/alert-message/alert-message.component";
import { AppComponent } from "src/app/components/app/app.component";
import { AuthenticationModule } from "src/app/authentication/authentication.module";
import { CanvasComponent } from "src/app/components/canvas/canvas.component";
import { ControlMenuComponent } from "src/app/components/control-menu/control-menu.component";
import { ErrorMessageComponent } from "src/app/components/error-message/error-message.component";
import { HomeComponent } from "src/app/components/home/home.component";
import { NavbarComponent } from "src/app/components/navbar/navbar.component";
import { ChatComponent } from "src/app/components/chat/chat.component";
import { NewDrawingAlertComponent } from "src/app/components/new-drawing/new-drawing-alert/new-drawing-alert.component";
import { NewDrawingFormComponent } from "src/app/components/new-drawing/new-drawing-form/new-drawing-form.component";
import { NewDrawingComponent } from "src/app/components/new-drawing/new-drawing.component";
import { ParameterMenuModule } from "./modules/parameter-menu/parameter-menu.module";
import { ParameterDirective } from "src/app/components/parameter-menu/parameter.directive";
import { SidenavComponent } from "src/app/components/sidenav/sidenav.component";
import { ToolParameterModule } from "src/app/components/tool-parameters/tool-parameter.module";
import { ToolsColorPickerComponent } from "src/app/components/tools-color-picker/tools-color-picker.component";
import { ToolsColorComponent } from "src/app/components/tools-color/tools-color.component";

@NgModule({
  declarations: [
    AppComponent,
    SidenavComponent,
    CanvasComponent,
    NewDrawingComponent,
    NewDrawingFormComponent,
    NewDrawingAlertComponent,
    ToolsColorComponent,
    ToolsColorPickerComponent,
    ControlMenuComponent,
    ParameterDirective,
    ErrorMessageComponent,
    AlertMessageComponent,
    HomeComponent,
    NavbarComponent,
    ChatComponent,
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
    // Modules from our app
    ParameterMenuModule,
    WorkspaceModule

  ],
  exports: [],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
