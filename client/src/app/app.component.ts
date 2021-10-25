import { Component, OnDestroy, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { HotkeysService } from 'src/app/modules/workspace';

@Component({
  selector: "app-root",
  styleUrls: ["./app.component.scss"],
  templateUrl: "./app.component.html",
})
export class AppComponent implements OnInit, OnDestroy {
  constructor(public dialog: MatDialog, private hotkeyService: HotkeysService) {
    this.hotkeyService.hotkeysListener();
  }

  public isChatOpen = false;

  openDialog() {}

  ngOnInit() {}

  ngOnDestroy(): void {}

  childChatEventClicked(value: boolean) {
    this.isChatOpen = value;
    console.log("CHAT OPEN: " + this.isChatOpen);
  }
}
