import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { TeamProfileComponent } from "./team-profile/team-profile.component";
import { TeamListingComponent } from "./team-listing/team-listing.component";
import { UsersRoutingModule } from "./team-routing.module";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { TeamCreationComponent } from "./team-creation/team-creation.component";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { ReactiveFormsModule } from "@angular/forms";
import { FormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";

@NgModule({
  declarations: [
    TeamProfileComponent,
    TeamListingComponent,
    TeamCreationComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
    MatFormFieldModule,
    MatGridListModule,
    MatInputModule,
    ReactiveFormsModule,
    UsersRoutingModule,
    FormsModule,
  ],
})
export class TeamModule {}
