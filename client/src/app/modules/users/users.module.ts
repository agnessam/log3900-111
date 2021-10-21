import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatInputModule } from "@angular/material/input";
import { TellMeAboutYourselfComponent } from "./tell-me-about-yourself/tell-me-about-yourself/tell-me-about-yourself.component";
import { UsersRoutingModule } from "./users-routing.module";
import { UserProfileComponent } from './user-profile/user-profile.component';

@NgModule({
  declarations: [TellMeAboutYourselfComponent, UserProfileComponent],
  imports: [
    MatCardModule,
    MatGridListModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule,
    CommonModule,
    FormsModule,
    UsersRoutingModule,
  ],
  exports: [],
})
export class UsersModule {}
