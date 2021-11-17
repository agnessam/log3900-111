import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { SettingsRoutingModule } from "./settings-routing.module";
import { SettingsContainerComponent } from "./settings-container/settings-container.component";
import { MatSidenavModule } from "@angular/material/sidenav";

@NgModule({
  declarations: [SettingsContainerComponent],
  imports: [CommonModule, SettingsRoutingModule, MatSidenavModule],
})
export class SettingsModule {}
