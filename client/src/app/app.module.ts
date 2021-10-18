import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MomentModule } from "ngx-moment";
import { MaterialModules } from "./app-material.module";
import { NavbarModule } from "./navbar/navbar.module";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { AuthenticationModule } from "./modules/authentication/authentication.module";
import { ChatModule } from "./modules/chat/chat.module";
import { WorkspaceModule } from "./modules/workspace/workspace.module";
import { ToolParameterModule } from "./modules/tool-parameters/tool-parameter.module";
import { NewDrawingModule } from "./modules/new-drawing/new-drawing.module";
import { SidenavModule } from "./modules/sidenav/sidenav.module";
import { ColorPickerModule } from "./modules/color-picker/color-picker.module";
import { SharedModule } from "./shared/shared.module";
import { ToolsColorModule } from "./modules/tools-color/tools-color.module";
import { ParameterMenuModule } from "./modules/parameter-menu/parameter-menu.module";
@NgModule({
  declarations: [AppComponent],
  imports: [
    WorkspaceModule,
    ChatModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MaterialModules,
    FontAwesomeModule,
    ToolParameterModule,
    MomentModule,
    AuthenticationModule,
    AppRoutingModule,
    NavbarModule,
    NewDrawingModule,
    SidenavModule,
    ColorPickerModule,
    SharedModule,
    ToolsColorModule,
    ParameterMenuModule,
  ],
  exports: [],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
