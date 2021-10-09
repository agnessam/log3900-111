import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder,FormGroup,Validators } from '@angular/forms';
import { User } from '../../interfaces/interface'


@Component({
  selector: 'app-test-database',
  templateUrl: './test-database.component.html',
  styleUrls: ['./test-database.component.scss']
})
export class TestDatabaseComponent implements OnInit {

  form: FormGroup;
  loading = false;
  submitted = false;
  currentUser: User;
  databaseurl: string;
  userdata : User;

  constructor(
              private fb: FormBuilder,
              private httpClient: HttpClient,
    ) { 
    

  }
  ngOnInit(): void {
            this.form = this.fb.group({
              firstName: ['', Validators.required],
              lastName: ['', Validators.required],
              username: ['', Validators.required],
              email: ['', Validators.required],
              password: ['',[Validators.required, Validators.minLength(6)]]
          });
  }

      // convenience getter for easy access to form fields
      get f() : FormGroup['controls'] { return this.form.controls; }

      


  onSubmit() {
    this.submitted = true;
     this.loading = true;  
      this.save();
  }


  save(): void {
      this.databaseurl = 'http://localhost:3000/api/v1/database/useraccountcreation';
      const body = {
        firstName: this.f.firstName.value,
        lastName: this.f.lastName.value,
        password: this.f.password.value,
        email: this.f.email.value,
        pseudonyme : this.f.username.value,
      };
      this.httpClient.post(this.databaseurl, body, {
        headers: new HttpHeaders()
          .set('Accept', 'application/json')
          .set('Access-Control-Allow-Origin', '*')
          .set('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,PATCH,OPTIONS'),
        responseType: 'text'
      })
        .toPromise()
        .then((result) => { alert(result); })
        .catch((e: Error) => { throw e;
        })
        ;
  }

}
