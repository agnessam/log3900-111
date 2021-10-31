import { Component, OnDestroy, OnInit, } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../authentication/models/user";

@Component({
  selector: "canal",
  templateUrl: "./canal.component.html",
  styleUrls: ["./canal.component.scss"],
})
export class CanalComponent implements OnInit, OnDestroy {
  public user: User | null;
  public chatSubscribiption: Subscription;
  public canals: string[] = ["hello", "world", "patate"];
  public canal: string;
  public chatStatus:boolean;

  //@Input() private chatRoomName: string = "default";

  constructor(
    private authenticationService: AuthenticationService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
    this.canal = "Canal name";
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

}
