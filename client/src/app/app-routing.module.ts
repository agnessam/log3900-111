import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./authentication/auth.guard";
import { PageNotFoundComponent } from "./components/error/page-not-found/page-not-found.component";
// import { HomeComponent } from "./components/home/home.component";
// import { SidenavComponent } from "./components/sidenav/sidenav.component";
import { ChatComponent } from "./components/chat/chat.component";

const routes: Routes = [
  // { path: "drawing", component: SidenavComponent, canActivate: [AuthGuard] },
  { path: "", component: ChatComponent, canActivate: [AuthGuard] },
  // { path: "", component: HomeComponent, canActivate: [AuthGuard] },
  { path: "**", component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
