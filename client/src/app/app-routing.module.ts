import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { PageNotFoundComponent } from "./components/error/page-not-found/page-not-found.component";
import { HomeComponent } from "./components/home/home.component";
import { SidenavComponent } from "./components/sidenav/sidenav.component";

const routes: Routes = [
  { path: "home", component: HomeComponent },
  { path: "drawing", component: SidenavComponent },
  { path: "", redirectTo: "home", pathMatch: "full" },
  { path: "**", component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
