import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChannelComponent } from "./channel.component";
import { MatIconModule } from "@angular/material/icon";

@NgModule({
  declarations: [ChannelComponent],
  imports: [CommonModule, MatIconModule],
  exports: [ChannelComponent,]
})
export class ChannelModule {}
