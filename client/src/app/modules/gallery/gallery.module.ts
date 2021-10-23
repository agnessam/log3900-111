import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { GalleryComponent } from "./gallery/gallery.component";
import { GalleryRoutingModule } from "./gallery-routing.module";
import { MatGridListModule } from "@angular/material/grid-list";

@NgModule({
  declarations: [GalleryComponent],
  imports: [CommonModule, GalleryRoutingModule, MatGridListModule],
})
export class GalleryModule {}
