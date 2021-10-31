import { GalleryComponent } from './modules/gallery/gallery/gallery.component';
import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./modules/authentication/";
import { PageNotFoundComponent } from "./modules/error/";
import { SidenavComponent } from "./modules/sidenav/sidenav.component";

const routes: Routes = [
  {
    path: "drawing",
    component: SidenavComponent,
    loadChildren: () =>
      import("./modules/sidenav/sidenav.module").then((m) => m.SidenavModule),
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
    component: GalleryComponent,
    loadChildren: () =>
      import("./modules/gallery/gallery.module").then((m) => m.GalleryModule),
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
