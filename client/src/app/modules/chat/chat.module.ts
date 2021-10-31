import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChatComponent } from "./chat.component";
import { MatIconModule } from "@angular/material/icon";
import { PopoutWindowComponent } from './popout-window/popout-window.component';

@NgModule({
  declarations: [ChatComponent, PopoutWindowComponent],
  imports: [CommonModule, MatIconModule],
  exports: [ChatComponent,]
})
export class ChatModule {}
