import {environment  } from './../../../../environments/environment';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { User } from '../../../interfaces/interface'
import * as CONSTANT from '../../../constants'
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class UseraccountparameterService {
 Database_URL : string;
 UserCredential : string[];
 Users : User [];

  constructor(private httpClient: HttpClient) { }

  // creation de compte utilisateur
   
  async CreateUser(User_DATA : User): Promise<void> {
    this.Database_URL= environment.serverRawURL+CONSTANT.CREATE_USER_ACCOUNT;
    console.log("dataurl ="+this.Database_URL);
    const body = {
      firstName: User_DATA.firstName ,
      lastName: User_DATA.lastName ,
      password: User_DATA.password ,
      email: User_DATA.email ,
      pseudonyme : User_DATA.pseudonyme ,
    };
   await this.httpClient.post(this.Database_URL, body, {
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

// login compte user  jefferson to it and add passport....
  async UserLogin (ConnexionParameter : string[]) : Promise<void>{
 
}

// Recuperer tout les utilisateurs existants
async GetAllUser(): Promise<void> {
  this.Database_URL= environment.serverRawURL+CONSTANT.GET_ALL_USER;
  await this.httpClient.get(this.Database_URL, {
  })
    .toPromise()
    .then((user: User[]) => {
      this.Users = user;
    })
    .catch((e: Error) => { throw e; })
    ;

}

// supprimer un compte utilisateur 
deleteDrawingById(pseudo : string): void {
  this.Database_URL= environment.serverRawURL+CONSTANT.DELETE_USER_ACCOUNT;
  this.httpClient.delete(this.Database_URL
  )
    .toPromise()
    .then(() => {
      alert('Compte utilisateur supprimer')
    })
    .catch((e: Error) => {alert('Impossible de supprimer le compte utilisateur')})
    ;
}



}
