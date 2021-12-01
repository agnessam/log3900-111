import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  ViewChild,
} from "@angular/core";
import { WorkspaceService } from "./workspace.service";

@Component({
  selector: "app-workspace",
  templateUrl: "./workspace.component.html",
  styleUrls: ["./workspace.component.scss"],
})
export class WorkspaceComponent implements OnInit, AfterViewInit {
  @ViewChild("workspaceEnv", { read: ElementRef })
  workspaceEnv: ElementRef;

  constructor(
    private el: ElementRef,
    private workspaceService: WorkspaceService
  ) {}

  ngOnInit(): void {
    this.workspaceService.el = this.el;
  }

  ngAfterViewInit(): void {
    this.workspaceService.scrolledElement = this.workspaceEnv;
  }
}
