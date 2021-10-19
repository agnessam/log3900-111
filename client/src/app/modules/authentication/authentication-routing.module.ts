import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "./login/login.component";
import { RegisterComponent } from "./register/register.component";

const authenticationRoutes: Routes = [
  { path: "users/login", component: LoginComponent },
  { path: "users/register", component: RegisterComponent },
];

@NgModule({
  imports: [RouterModule.forChild(authenticationRoutes)],
  exports: [RouterModule],
})
export class AuthenticationRoutingModule {}
