import { Component, OnDestroy, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ColorSchemeService } from "./core/themes/color-scheme.service";

@Component({
  selector: "app-root",
  styleUrls: ["./app.component.scss"],
  templateUrl: "./app.component.html",
})
export class AppComponent implements OnInit, OnDestroy {
  userId: string | null;

  constructor(
    public dialog: MatDialog,
    private colorSchemeService: ColorSchemeService
  ) {
    this.colorSchemeService.load();
    this.colorSchemeService.update("light");
  }

  openDialog() {}

  ngOnInit() {}

  ngAfterViewChecked() {
    this.userId = localStorage.getItem("userId");
  }

  ngOnDestroy(): void {}
}
