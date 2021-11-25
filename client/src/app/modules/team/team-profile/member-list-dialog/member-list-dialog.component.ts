import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { User } from "src/app/modules/users/models/user";

@Component({
  selector: "app-member-list-dialog",
  templateUrl: "./member-list-dialog.component.html",
  styleUrls: ["./member-list-dialog.component.scss"],
})
export class MemberListDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { members: User[] },
    private dialogRef: MatDialogRef<MemberListDialogComponent>
  ) {}

  ngOnInit(): void {}

  onCancel(): void {
    this.dialogRef.close();
  }
}
