import { TestBed } from '@angular/core/testing';

import { PopoutChatService } from './popout-chat.service';

describe('PopoutChatService', () => {
  let service: PopoutChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PopoutChatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
