import { Component, Input, OnInit } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { Router } from "@angular/router";
import { Drawing } from "src/app/shared";

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
}
