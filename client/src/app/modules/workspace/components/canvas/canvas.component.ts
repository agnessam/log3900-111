import {
  AfterViewInit,
  Component,
  ElementRef,
  HostListener,
  OnDestroy,
  Renderer2,
  ViewChild,
} from "@angular/core";
import { Subscription } from "rxjs";
import { DrawingService, ToolsService } from "src/app/modules/workspace";

/// S'occupe d'afficher le svg dans un component
@Component({
  selector: "app-canvas",
  templateUrl: "./canvas.component.html",
  styleUrls: ["./canvas.component.scss"],
})
export class CanvasComponent implements AfterViewInit, OnDestroy {
  @ViewChild("svgCanvas")
  canvasDiv: ElementRef;

  svg: SVGElement;
  sub: Subscription;

  constructor(
    private drawingService: DrawingService,
    public renderer: Renderer2,
    private toolsService: ToolsService
  ) {
    this.drawingService.renderer = this.renderer;
    this.drawingService.isCreated = true;
  }

  /// À l'initialisation, le canvas s'abonne au service de dessin pour reçevoir en string le svg
  ngAfterViewInit() {
    this.sub = this.drawingService.drawingEmit.subscribe((el: SVGElement) => {
      if (this.svg) {
        this.renderer.removeChild(this.canvasDiv.nativeElement, this.svg);
      }
      this.svg = el;
      this.renderer.appendChild(this.canvasDiv.nativeElement, this.svg);
    });
  }

  ngOnDestroy() {
    this.drawingService.isCreated = false;
  }

  /// la longueur de la zone de dessin
  get height(): number {
    return this.drawingService.isCreated ? this.drawingService.height : 0;
  }

  /// la largueur de la zone de dessin
  get width(): number {
    return this.drawingService.isCreated ? this.drawingService.width : 0;
  }

  /// la couleur de fond de la zone de dessin
  get backgroundColor(): string {
    return this.drawingService.rgbaColorString;
  }

  /// l'opacite de la zone de dessin
  get backgroundAlpha(): number {
    return this.drawingService.alpha;
  }

  get isDrawingCreated(): boolean {
    return this.drawingService.isCreated;
  }

  /// Effectue un onPress sur le clique droit de la sourie
  onRightClick(event: MouseEvent): boolean {
    return false;
  }

  /// Effectue un onPress sur le clique gauche de la sourie
  onMouseDown(event: MouseEvent): void {
    this.toolsService.onPressed(event);
  }

  /// Effectue un onRelease quand le clique de la sourie est relaché
  @HostListener("window:mouseup", ["$event"])
  onMouseUp(event: MouseEvent): void {
    this.toolsService.onRelease(event);
  }

  /// Effectue un onMove quand la sourie bouge
  @HostListener("window:mousemove", ["$event"])
  onMouseMove(event: MouseEvent): void {
    this.toolsService.onMove(event);
  }
}
