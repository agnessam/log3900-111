import { AfterViewInit, Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { SocketRoomInformation } from "src/app/shared/socket/socket-room-information";
import { TextChannel } from "../../chat/models/text-channel.model";
import { ChatSocketService } from "../../chat/services/chat-socket.service";
import { TextChannelService } from "../../chat/services/text-channel.service";
import { DrawingService } from "../../workspace";
import { DrawingSocketService } from "../../workspace/services/synchronisation/sockets/drawing-socket/drawing-socket.service";
@Component({
  selector: "app-drawing",
  templateUrl: "./drawing.component.html",
  styleUrls: ["./drawing.component.scss"],
})
export class DrawingComponent implements OnInit, AfterViewInit {
  socketInformation: SocketRoomInformation;
  drawingId: string;
  channelSocketInfo: SocketRoomInformation;
  collaborationChannel: TextChannel | undefined;
  constructor(
    private route: ActivatedRoute,
    private drawingService: DrawingService,
    private drawingSocketService: DrawingSocketService,
    private chatSocketService: ChatSocketService,
    private textChannelService: TextChannelService,
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.drawingId = params["id"];
      this.drawingService.drawingId = this.drawingId;
      this.socketInformation = {
        roomName: this.drawingId,
        userId: localStorage.getItem("userId")!,
      };
      this.drawingSocketService.connect();
      this.drawingSocketService.joinRoom(this.socketInformation);

      this.textChannelService.getChannels().subscribe((channels) => {
        this.collaborationChannel = channels.find((channel) => channel.drawing === this.socketInformation.roomName);
        if (this.collaborationChannel !== undefined) {
          this.channelSocketInfo = {
            roomName: this.collaborationChannel.name,
            userId: localStorage.getItem("userId")!,
          }
          this.chatSocketService.joinRoom(this.channelSocketInfo);
          this.textChannelService.emitJoinCollaboration(this.collaborationChannel);
        }
      })
    });
  }

  ngAfterViewInit(): void {
    this.drawingSocketService.sendGetUpdateDrawingRequest();
  }

  ngOnDestroy(): void {
    this.drawingService.saveDrawing();
    this.drawingSocketService.leaveRoom(this.socketInformation);
    this.chatSocketService.leaveRoom(this.channelSocketInfo);
    if (this.collaborationChannel !== undefined) {
      this.textChannelService.emitLeaveCollaboration(this.collaborationChannel);
    }
  }
}
