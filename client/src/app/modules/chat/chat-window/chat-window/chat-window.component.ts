import { CdkPortal, DomPortalOutlet } from '@angular/cdk/portal';
import { AfterViewInit, ApplicationRef, Component, ComponentFactoryResolver, Injector, ViewChild } from '@angular/core';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.scss']
})
export class ChatWindowComponent implements AfterViewInit {

  @ViewChild(CdkPortal) portal: CdkPortal;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private applicationRef: ApplicationRef,
    private injector: Injector,
  ) { }

  ngAfterViewInit(): void {
    const externalWindow = window.open(
      '',
      '',
      'width=600,height=400,left=200,top=200',
    );

    // create a PortalHost with the body of the new window document
    if (externalWindow) {
      const host = new DomPortalOutlet(
        externalWindow.document.body,
        this.componentFactoryResolver,
        this.applicationRef,
        this.injector,
      );
      // Attach the portal
      host.attach(this.portal);
    }
  }

}
