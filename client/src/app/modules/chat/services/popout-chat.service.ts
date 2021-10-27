import { ComponentPortal, DomPortalOutlet } from '@angular/cdk/portal';
import { ApplicationRef, ComponentFactoryResolver, ComponentRef, Injectable, Injector, OnDestroy } from '@angular/core';
import { ChatComponent } from 'src/app/modules/chat/chat.component';

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

  openPopout() {
    // open window without url
    const windowInstance = window.open('' , '', '');

    // Wait for window instance to be created
    setTimeout(() => {
      this.createCDKPortal(windowInstance);
    }, 1000);

  }

  createCDKPortal(windowInstance: Window | null) {
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
        // const injector = this.createInjector(data);

        // Attach the portal
        windowInstance.document.title = 'Chat';
        this.attachCustomerContainer(outlet, this.injector);

        // POPOUT_MODALS['windowInstance'] = windowInstance;
        // POPOUT_MODALS['outlet'] = outlet;
        // POPOUT_MODALS['componentInstance'] = componentInstance;
      };
    }
  }

  attachCustomerContainer(outlet: DomPortalOutlet, injector: Injector | null | undefined) {
    const containerPortal = new ComponentPortal(ChatComponent, null, injector);
    const containerRef: ComponentRef<ChatComponent> = outlet.attach(containerPortal);
    return containerRef.instance;
  }

  // createInjector(data): PortalInjector {
  //   const injectionTokens = new WeakMap();
  //   injectionTokens.set(POPOUT_MODAL_DATA, data);
  //   return new PortalInjector(this.injector, injectionTokens);
  // }

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
}
