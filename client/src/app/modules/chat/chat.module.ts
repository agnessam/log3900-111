import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChatComponent } from "./chat.component";
import { MatIconModule } from "@angular/material/icon";

@NgModule({
  declarations: [ChatComponent],
  imports: [CommonModule, MatIconModule],
  exports: [ChatComponent,]
})
export class ChatModule {}
