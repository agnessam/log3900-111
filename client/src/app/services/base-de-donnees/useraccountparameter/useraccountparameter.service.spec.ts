import { TestBed } from '@angular/core/testing';

import { UseraccountparameterService } from './useraccountparameter.service';

describe('UseraccountparameterService', () => {
  let service: UseraccountparameterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UseraccountparameterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
