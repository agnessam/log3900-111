import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { SettingsContainerComponent } from "./settings-container/settings-container.component";
import { UserSettingsComponent } from "./settings-container/user-settings/user-settings.component";

const routes: Routes = [
  {
    path: "",
    component: SettingsContainerComponent,
    children: [
      {
        path: "users",
        component: UserSettingsComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SettingsRoutingModule {}
