import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../modules/authentication/models/user";
import { ChatService } from "../modules/chat/services/chat.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.scss"],
})
export class NavbarComponent implements OnInit {
  public user: User | null;
  public searchQuery: string;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private chatService: ChatService
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
  }

  ngOnInit(): void {
    this.chatService.toggleChannelOverlay.subscribe();
  }

  search(): void {
    this.router.navigate(["/search"], { queryParams: { q: this.searchQuery } });
  }

  goToProfile(): void {
    this.router.navigate(["/users/" + this.user?._id]);
  }

  goToSettings(): void {
    this.router.navigate(["/settings"]);
  }

  logout(): void {
    this.authenticationService.logout().subscribe((response) => {
      console.log(response);
    });
    this.router.navigate(["/login"]);
  }

  onChatClick(): void {
    this.chatService.toggleChannelOverlay.emit();
  }
}
