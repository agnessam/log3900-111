import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { TellMeAboutYourselfComponent } from "./tell-me-about-yourself/tell-me-about-yourself/tell-me-about-yourself.component";

const userRoutes: Routes = [
  { path: "customize", component: TellMeAboutYourselfComponent },
];

@NgModule({
  imports: [RouterModule.forChild(userRoutes)],
  exports: [RouterModule],
})
export class UsersRoutingModule {}
