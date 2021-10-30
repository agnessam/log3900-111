import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CanalComponent } from "./canal.component";
import { MatIconModule } from "@angular/material/icon";

@NgModule({
  declarations: [CanalComponent],
  imports: [CommonModule, MatIconModule],
  exports: [CanalComponent,]
})
export class CanalModule {}
