import { Component, OnDestroy, OnInit, } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../authentication/models/user";
import { ChatService } from "../chat/services/chat.service";

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

  public canalOverlayStatus:boolean;

  //@Input() private chatRoomName: string = "default";

  constructor(
    private authenticationService: AuthenticationService,
    private chatService:ChatService
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
    this.canal = "Canal name";
  }

  // Si tu veux creer un nouveau canal:
  // this.textChannelService.createChannel('testname', this.user?._id as string).subscribe((channel) => {
  //   console.log(channel);
  // });

  ngOnInit(): void {
    this.chatService.toggleCanalOverlay.subscribe(status=>{
      this.canalOverlayStatus = status;
      this.toggleCanalOverlay(status);
    });
  }

  ngOnDestroy(): void {
  }

  addCanal():void {

  }

  searchCanal():void {

  }

  openCanal(canalName: string):void {
    let canalOverlay = <HTMLInputElement>document.getElementById("canal-overlay");
    canalOverlay.style.display = "none";
    this.chatService.toggleChatOverlay.emit(canalName);
    this.toggleCanalOverlay(true);
    this.canalOverlayStatus = false;
  }

  toggleCanalOverlay(isOpen:boolean){
    let canalOverlay = <HTMLInputElement>document.getElementById("canal-overlay");

    if(isOpen) canalOverlay.style.display = "none";
    else canalOverlay.style.display = "block";
  }
}
