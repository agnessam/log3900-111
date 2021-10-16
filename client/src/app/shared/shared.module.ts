import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertMessageComponent } from "./alert-message/alert-message.component";
import { ErrorMessageComponent } from "./error-message/error-message.component";

@NgModule({
  declarations: [AlertMessageComponent, ErrorMessageComponent],
  imports: [
    CommonModule
  ]
})
export class SharedModule { }
