import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostDialogComponent } from './post-dialog.component';
import { AvatarModule } from 'ngx-avatar';
import { MatIconModule } from '@angular/material/icon';



@NgModule({
  declarations: [
    PostDialogComponent
  ],
  imports: [
    CommonModule,
    AvatarModule,
    MatIconModule,
  ]
})
export class PostDialogModule { }
