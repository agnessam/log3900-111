import { Component, OnDestroy, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { HotkeysService } from "src/app/services/hotkeys/hotkeys.service";

@Component({
  selector: "app-root",
  styleUrls: ["./app.component.scss"],
  templateUrl: "./app.component.html",
})
export class AppComponent implements OnInit, OnDestroy {
  constructor(public dialog: MatDialog, private hotkeyService: HotkeysService) {
    this.hotkeyService.hotkeysListener();
  }

  openDialog() {}

  ngOnInit() {}

  ngOnDestroy(): void {}
}
