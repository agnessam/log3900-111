import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChatComponent } from "./chat.component";
import { MatIconModule } from "@angular/material/icon";
import { PopoutWindowComponent } from './popout-window/popout-window.component';
import { ChannelComponent } from "./channel/channel.component";
import { TextChannelService } from "./services/text-channel.service";
import { MatTabsModule } from "@angular/material/tabs";
import { FormsModule } from "@angular/forms";
import { SharedModule } from "src/app/shared/shared.module";

@NgModule({
  declarations: [ChatComponent, PopoutWindowComponent, ChannelComponent],
  imports: [CommonModule, SharedModule, MatIconModule, MatTabsModule, FormsModule,],
  providers: [TextChannelService],
  exports: [ChatComponent, ChannelComponent]
})
export class ChatModule {}
