import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { TeamMainPageComponent } from "./team-main-page/team-main-page/team-main-page.component";

const routes: Routes = [{ path: "", component: TeamMainPageComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeamRoutingModule {}
