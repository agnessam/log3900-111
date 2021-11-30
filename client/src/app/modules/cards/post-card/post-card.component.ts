import { Component, Input, OnInit } from "@angular/core";
import { DomSanitizer } from "@angular/platform-browser";
import { Post } from "src/app/shared/models/post.model";

@Component({
  selector: "app-post-card",
  templateUrl: "./post-card.component.html",
  styleUrls: ["./post-card.component.scss"],
})
export class PostCardComponent implements OnInit {
  @Input() post: Post;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit(): void {}

  getSanitizedUrl(dataUri: string) {
    return this.sanitizer.bypassSecurityTrustUrl(dataUri);
  }
}
