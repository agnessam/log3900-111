import { Component, OnInit ,Output, EventEmitter} from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../modules/authentication/models/user";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.scss"],
})
export class NavbarComponent implements OnInit {
  public user: User | null;
  public isChatOpened: boolean;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
    this.isChatOpened=true;
  }

  ngOnInit(): void {}

  logout(): void {
    this.authenticationService.logout().subscribe((response) => {
      console.log(response);
    });
    this.router.navigate(["/login"]);
  }


  @Output() openChatEvent: EventEmitter<boolean> = new EventEmitter();

  onChatClick(value: boolean) :void {
    this.isChatOpened = !this.isChatOpened;
    this.openChatEvent.emit(value);
  }

}
