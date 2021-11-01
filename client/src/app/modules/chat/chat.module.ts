import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChatComponent } from "./chat.component";
import { MatIconModule } from "@angular/material/icon";
import { PopoutWindowComponent } from './popout-window/popout-window.component';
import { ChannelComponent } from "./channel/channel.component";

@NgModule({
  declarations: [ChatComponent, PopoutWindowComponent, ChannelComponent],
  imports: [CommonModule, MatIconModule],
  exports: [ChatComponent, ChannelComponent]
})
export class ChatModule {}
