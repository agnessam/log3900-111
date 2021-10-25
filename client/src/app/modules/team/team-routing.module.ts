import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { TeamListingComponent } from "./team-listing/team-listing.component";
import { TeamProfileComponent } from "./team-profile/team-profile.component";

const userRoutes: Routes = [
  { path: "", component: TeamListingComponent },
  { path: ":name", component: TeamProfileComponent },
];

@NgModule({
  imports: [RouterModule.forChild(userRoutes)],
  exports: [RouterModule],
})
export class UsersRoutingModule {}
