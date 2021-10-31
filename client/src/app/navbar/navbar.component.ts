import { Component, OnInit} from "@angular/core";
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
  public channelOverlayStatus:boolean = false;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private chatService:ChatService
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
  }

  ngOnInit(): void {
    this.chatService.toggleChannelOverlay.subscribe(status=>this.channelOverlayStatus = status);
  }

  logout(): void {
    this.authenticationService.logout().subscribe((response) => {
      console.log(response);
    });
    this.router.navigate(["/login"]);
  }

  onChatClick() :void {
    this.chatService.toggleChannelOverlay.emit(!this.channelOverlayStatus);
  }

}
