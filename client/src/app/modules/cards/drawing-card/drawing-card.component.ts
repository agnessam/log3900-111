import { Component, Input, OnInit } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { Router } from "@angular/router";
import { Drawing } from "src/app/shared";
import { Team } from "src/app/shared/models/team.model";
import { User } from "../../users/models/user";

@Component({
  selector: "app-drawing-card",
  templateUrl: "./drawing-card.component.html",
  styleUrls: ["./drawing-card.component.scss"],
})
export class DrawingCardComponent implements OnInit {
  @Input() drawing: Drawing;

  constructor(private router: Router, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {}

  getSanitizedUrl(dataUri: string) {
    return this.sanitizer.bypassSecurityTrustUrl(dataUri);
  }

  goToDrawing() {
    this.router.navigate([`/drawings/${this.drawing._id}`]);
  }

  isUser(owner: any): owner is User {
    return "username" in owner;
  }

  isTeam(owner: any): owner is Team {
    return "name" in owner;
  }

  isProtected(): boolean {
    if (this.drawing.privacyLevel == "protected") return true;
    return false;
  }

  capitalizeFirstLetter(string: string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }
}
