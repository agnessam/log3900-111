import { Component, OnInit } from "@angular/core";
import { ColorSchemeService } from "src/app/core/themes/color-scheme.service";

@Component({
  selector: "app-application-settings",
  templateUrl: "./application-settings.component.html",
  styleUrls: ["./application-settings.component.scss"],
})
export class ApplicationSettingsComponent implements OnInit {
  constructor(public colorSchemeService: ColorSchemeService) {
    this.colorSchemeService.load();
  }

  ngOnInit(): void {}

  setCurrentTheme() {
    if (this.colorSchemeService.currentActive() == "dark") {
      return true;
    } else {
      return false;
    }
  }

  toggleDarkTheme(event: any) {
    if (event.checked) {
      this.colorSchemeService.update("dark");
    } else {
      this.colorSchemeService.update("light");
    }
  }
}
