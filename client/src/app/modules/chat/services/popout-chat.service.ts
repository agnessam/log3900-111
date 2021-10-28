import { ComponentPortal, DomPortalOutlet } from '@angular/cdk/portal';
import { ApplicationRef, ComponentFactoryResolver, ComponentRef, Injectable, Injector, OnDestroy } from '@angular/core';
import { ChatComponent } from 'src/app/modules/chat/chat.component';
import { POPOUT_MODALS, POPOUT_MODAL_DATA } from '../models/popout.tokens';

@Injectable({
  providedIn: 'root',
})
export class PopoutChatService implements OnDestroy {
  styleSheetElement: HTMLLinkElement;

  constructor(
    private injector: Injector,
    private componentFactoryResolver: ComponentFactoryResolver,
    private applicationRef: ApplicationRef,
    ) { }

  ngOnDestroy() {}

  openPopout(data: any) {
    // open window without url
    const windowInstance = window.open('' , '', '');

    // Wait for window instance to be created
    setTimeout(() => {
      this.createCDKPortal(data, windowInstance);
    }, 1000);

  }

  createCDKPortal(data: any, windowInstance: Window | null) {
    if (windowInstance) {
      // Create a PortalOutlet with the body of the new window document
      const outlet = new DomPortalOutlet(windowInstance.document.body, this.componentFactoryResolver, this.applicationRef, this.injector);
      // Copy styles from parent window
      document.querySelectorAll('style').forEach((htmlElement) => {
        windowInstance.document.head.appendChild(htmlElement.cloneNode(true));
      });
      // Copy stylesheet link from parent window
      this.styleSheetElement = this.getStyleSheetElement();
      windowInstance.document.head.appendChild(this.styleSheetElement);

      this.styleSheetElement.onload = () => {
        // Clear popout modal content
        windowInstance.document.body.innerText = '';

        // Create an injector with modal data
        this.injector = this.createInjector(data);

        // Attach the portal
        windowInstance.document.title = 'Chat';
        const componentInstance = this.attachContainer(outlet, this.injector);

        POPOUT_MODALS.windowInstance = windowInstance;
        POPOUT_MODALS.outlet = outlet;
        POPOUT_MODALS.componentInstance = componentInstance;
      };
    }
  }

  createInjector(data: any): Injector {
    // const injectionTokens = new WeakMap();
    // injectionTokens.set(POPOUT_MODAL_DATA, data);
    return Injector.create({
      parent: this.injector,
      providers: [
        {provide: POPOUT_MODAL_DATA, useValue: data}
      ]
    });
  }

  attachContainer(outlet: DomPortalOutlet, injector: Injector | null | undefined) {
    const containerPortal = new ComponentPortal(ChatComponent, null, injector);
    const containerRef: ComponentRef<ChatComponent> = outlet.attach(containerPortal);
    return containerRef.instance;
  }

  getStyleSheetElement() {
    const styleSheetElement = document.createElement('link');
    document.querySelectorAll('link').forEach((htmlElement) => {
      if (htmlElement.rel === 'stylesheet') {
        const absoluteUrl = new URL(htmlElement.href).href;
        styleSheetElement.rel = 'stylesheet';
        styleSheetElement.href = absoluteUrl;
      }
    });
    return styleSheetElement;
  }

  isPopoutWindowOpen() {
    return POPOUT_MODALS.windowInstance && !POPOUT_MODALS.windowInstance.closed;
  }

  closePopoutModal() {
    Object.keys(POPOUT_MODALS).forEach(() => {
      if (POPOUT_MODALS.windowInstance) {
        POPOUT_MODALS.windowInstance.close();
      }
    });
  }
}
