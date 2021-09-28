import http from "http";
import { inject, injectable } from "inversify";
import { Application } from "./app";
import { ChatSocketService } from "./services/sockets/chat-socket.service";
import { TYPES } from "./types";
import { Server as SocketServer } from "socket.io";

@injectable()
export class Server {
  private readonly appPort: string | number | boolean = this.normalizePort(
    process.env.PORT || "3000"
  );
  private readonly baseTen: number = 10;
  private server: http.Server;
  private socketServer: SocketServer;

  constructor(
    @inject(TYPES.Application) private application: Application,
    @inject(TYPES.ChatSocketService)
    private chatSocketService: ChatSocketService
  ) {}

  init(): void {
    this.application.app.set("port", this.appPort);

    this.server = http.createServer(this.application.app);

    this.server.listen(this.appPort);
    this.server.on("error", (error: NodeJS.ErrnoException) =>
      this.onError(error)
    );
    this.server.on("listening", () => this.onListening());

    this.initalizeSocketServices();
  }

  private normalizePort(val: number | string): number | string | boolean {
    const port: number =
      typeof val === "string" ? parseInt(val, this.baseTen) : val;
    if (isNaN(port)) {
      return val;
    } else if (port >= 0) {
      return port;
    } else {
      return false;
    }
  }

  private onError(error: NodeJS.ErrnoException): void {
    if (error.syscall !== "listen") {
      throw error;
    }
    const bind: string =
      typeof this.appPort === "string"
        ? "Pipe " + this.appPort
        : "Port " + this.appPort;
    switch (error.code) {
      case "EACCES":
        console.error(`${bind} requires elevated privileges`);
        process.exit(1);
      case "EADDRINUSE":
        console.error(`${bind} is already in use`);
        process.exit(1);
      default:
        throw error;
    }
  }

  private onListening(): void {
    const addr = this.server.address();
    // tslint:disable-next-line:no-non-null-assertion
    const bind: string =
      typeof addr === "string" ? `pipe ${addr}` : `port ${addr!.port}`;
    // tslint:disable-next-line:no-console
    console.log(`Listening on ${bind}`);
  }

  // Initialise tous les services reliés à SocketIO
  private initalizeSocketServices(): void {
    this.socketServer = new SocketServer(this.server, {
      cors: {},
    });
    this.chatSocketService.init(this.socketServer);
  }
}
