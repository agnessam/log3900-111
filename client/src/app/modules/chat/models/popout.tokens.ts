import { InjectionToken } from '@angular/core';
import { User } from '../../users/models/user';

export interface PopoutData {
  id: number;
  owner: User | null;
  chatRoomName: string;
}

export const POPOUT_MODAL_DATA = new InjectionToken<PopoutData>('POPOUT_MODAL_DATA');

export let POPOUT_MODALS = {
    windowInstance: document.createElement('window') as unknown as Window,
    componentInstance: {},
    outlet: {},
};