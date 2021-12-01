import { Component, OnDestroy, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { HotkeysService } from "src/app/modules/workspace";
import { ColorSchemeService } from "./core/themes/color-scheme.service";

@Component({
  selector: "app-root",
  styleUrls: ["./app.component.scss"],
  templateUrl: "./app.component.html",
})
export class AppComponent implements OnInit, OnDestroy {
  constructor(
    public dialog: MatDialog,
    private hotkeyService: HotkeysService,
    private colorSchemeService: ColorSchemeService
  ) {
    this.hotkeyService.hotkeysListener();
    this.colorSchemeService.load();
    this.colorSchemeService.update("dark");
  }

  openDialog() {}

  ngOnInit() {}

  ngOnDestroy(): void {}
}
