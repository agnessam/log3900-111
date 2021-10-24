import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { GalleryComponent } from "./gallery/gallery.component";
import { GalleryRoutingModule } from "./gallery-routing.module";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatButtonModule } from "@angular/material/button";
import { NewDrawingModule } from "../new-drawing/new-drawing.module";
import { MatDialogModule } from "@angular/material/dialog";
import { WorkspaceModule } from "../workspace/workspace.module";
import { DrawingService } from "../workspace";

@NgModule({
  declarations: [GalleryComponent],
  imports: [
    CommonModule,
    GalleryRoutingModule,
    MatButtonModule,
    MatDialogModule,
    MatGridListModule,
    NewDrawingModule,
    WorkspaceModule,
  ],
  providers: [DrawingService],
})
export class GalleryModule {}
