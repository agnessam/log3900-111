import { TestBed } from '@angular/core/testing';

import { SelectionStartCommandService } from './selection-start-command.service';

describe('SelectionStartCommandService', () => {
  let service: SelectionStartCommandService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SelectionStartCommandService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
