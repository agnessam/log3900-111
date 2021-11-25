import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { User } from "src/app/modules/users/models/user";

@Component({
  selector: "app-member-list-dialog",
  templateUrl: "./member-list-dialog.component.html",
  styleUrls: ["./member-list-dialog.component.scss"],
})
export class MemberListDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { members: User[] },
    private dialogRef: MatDialogRef<MemberListDialogComponent>,
    private router: Router
  ) {}

  ngOnInit(): void {}

  goToProfile(userId: string): void {
    this.dialogRef.close();
    this.router.navigate([`/users/${userId}`]);
  }
  onCancel(): void {
    this.dialogRef.close();
  }
}
