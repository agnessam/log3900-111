import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChatComponent } from "./chat.component";
import { MatIconModule } from "@angular/material/icon";
import { PopoutWindowComponent } from './popout-window/popout-window.component';
import { ChannelComponent } from "./channel/channel.component";
import { TextChannelService } from "./services/text-channel.service";

@NgModule({
  declarations: [ChatComponent, PopoutWindowComponent, ChannelComponent],
  imports: [CommonModule, MatIconModule],
  providers: [TextChannelService],
  exports: [ChatComponent, ChannelComponent]
})
export class ChatModule {}
