import { Component, OnDestroy, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ColorSchemeService } from "./core/themes/color-scheme.service";

@Component({
  selector: "app-root",
  styleUrls: ["./app.component.scss"],
  templateUrl: "./app.component.html",
})
export class AppComponent implements OnInit, OnDestroy {
  constructor(
    public dialog: MatDialog,
    private colorSchemeService: ColorSchemeService
  ) {
    this.colorSchemeService.load();
    this.colorSchemeService.update("light");
  }

  openDialog() {}

  ngOnInit() {}

  ngOnDestroy(): void {}
}
