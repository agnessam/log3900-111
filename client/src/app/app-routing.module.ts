import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./modules/authentication/";
import { PageNotFoundComponent } from "./modules/error/";
import { ChatComponent } from "./modules/chat/chat.component";

const routes: Routes = [
  {
    path: "chat",
    component: ChatComponent,
    loadChildren: () =>
      import("./modules/chat/chat.module").then((m) => m.ChatModule),
    canActivate: [AuthGuard],
  },
  {
    path: "users",
    loadChildren: () =>
      import("./modules/users/users.module").then((m) => m.UsersModule),
  },
  {
    path: "gallery",
    loadChildren: () =>
      import("./modules/gallery/gallery.module").then((m) => m.GalleryModule),
  },
  {
    path: "",
    component: ChatComponent,
    loadChildren: () =>
      import("./modules/chat/chat.module").then((m) => m.ChatModule),
    canActivate: [AuthGuard],
  },
  {
    path: "**",
    component: PageNotFoundComponent,
    loadChildren: () =>
      import("./modules/error/error.module").then((m) => m.ErrorModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
