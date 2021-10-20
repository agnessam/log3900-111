import { CdkPortal, DomPortalOutlet } from '@angular/cdk/portal';
import { ApplicationRef, Component, ComponentFactoryResolver, Injector, OnDestroy, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-external-chat',
  templateUrl: './external-chat.component.html',
  styleUrls: ['./external-chat.component.scss']
})
export class ExternalChatComponent implements OnInit, OnDestroy {
  // Reference to the portal
  @ViewChild(CdkPortal) portal: CdkPortal;
  // save a reference to the window so we can close it
  private externalWindow: Window | null;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private applicationRef: ApplicationRef,
    private injector: Injector,
  ) { }

  ngOnInit(): void {
    // creating external window
    this.externalWindow = window.open('', '', 'width=600,height=400,left=200,top=200');

    // create a PortalHost with the body of the new window document
    if (this.externalWindow) {
      const host = new DomPortalOutlet(
      this.externalWindow.document.body,
      this.componentFactoryResolver,
      this.applicationRef,
      this.injector,
      );
      // Attach the portal
      host.attach(this.portal);
    }
  }

  ngOnDestroy(): void {
    // close window when component is destroyed
    this.externalWindow?.close();
  }

}
