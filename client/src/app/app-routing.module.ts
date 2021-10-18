import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./modules/authentication/";
// import { PageNotFoundComponent } from "./components/error/page-not-found/page-not-found.component";
import { SidenavComponent } from "./modules/sidenav/sidenav.component";
import { ChatComponent } from "./modules/chat/chat.component";

const routes: Routes = [
  {
    path: "drawing",
    component: SidenavComponent,
    loadChildren: () =>
      import("./modules/sidenav/sidenav.module").then((m) => m.SidenavModule),
    canActivate: [AuthGuard],
  },
  {
    path: "chat",
    component: ChatComponent,
    loadChildren: () =>
      import("./modules/chat/chat.module").then((m) => m.ChatModule),
    canActivate: [AuthGuard],
  },
  {
    path: "blah",
    component: ChatComponent,
    loadChildren: () =>
      import("./modules/chat/chat.module").then((m) => m.ChatModule),
    canActivate: [AuthGuard],
  },
  // { path: "**", component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
