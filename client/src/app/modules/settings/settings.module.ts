import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { SettingsRoutingModule } from "./settings-routing.module";
import { SettingsContainerComponent } from "./settings-container/settings-container.component";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatButtonModule } from "@angular/material/button";
import { UserSettingsComponent } from "./settings-container/user-settings/user-settings.component";
import { UsersModule } from "../users/users.module";

@NgModule({
  declarations: [SettingsContainerComponent, UserSettingsComponent],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    UsersModule,
    MatSidenavModule,
    MatButtonModule,
  ],
})
export class SettingsModule {}
