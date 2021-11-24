import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Team } from "src/app/shared/models/team.model";

@Component({
  selector: "app-confirm-delete-dialog",
  templateUrl: "./confirm-delete-dialog.component.html",
  styleUrls: ["./confirm-delete-dialog.component.scss"],
})
export class ConfirmDeleteDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { team: Team },
    private dialogRef: MatDialogRef<ConfirmDeleteDialogComponent>
  ) {}

  ngOnInit(): void {}

  deleteTeam(): void {
    console.log("hihi");
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
