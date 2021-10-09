import { injectable } from 'inversify';
import { Collection, MongoClient, Filter} from 'mongodb';
import { User } from '../../../client/src/app/interfaces/interface';
import * as ENV from '../environnement/env';
import 'reflect-metadata';

@injectable()
export class DatabaseService {   
    UserCollection: Collection<User>;
    ExistingUserData : User ;
    logginCredential : string [];
    UniquePseudo : boolean;
    UserDataLenght : boolean;


    constructor() {
     // Connexion a la base de données MongoDb et la collection UserParametrer      
        MongoClient.connect(ENV.DATABASE_URL)
            .then((client: MongoClient) => {            
                this.UserCollection = client.db(ENV.DATABASE_NAME).collection(ENV.DATABASE_COLLECTION[0]);
                console.log('Connexion a la base de donnée reussie'); 
            })
            .catch(() => {
                console.log('Erreur de connexion. Impossible de se connecter a la base de donnée');
                process.exit(1);
            });

    }
    // creation de compte utilisateur
        async addUserData(data: User): Promise<void> {
            if (this.validateUserdata(data)) {
                console.log("creation de user dans le if")
                this.UserCollection.insertOne(data).catch((error: Error) => {
                    throw error;
                });
            } else {
                alert('Invalide user data');
            }
        }

    // Recuperer tous les utilisateurs existants aainsi que leurs parametres
        async getAll(): Promise<User[]> {
            return  this.UserCollection.find({}).toArray()
                    .then((USERPARAM: User[]) => {
                        return USERPARAM;
                    })
                    .catch((error: Error) => {
                        throw  error;
                    });
        }

    // Supprimer un compte  utilisateur 
        async deleteuseraccount(pseudo: string): Promise<void> {
            return this.UserCollection
                .findOneAndDelete({ username: pseudo })
            .then(() => {
                alert('dessin supprimer de la base de donnée');
            })
            .catch((error: Error) => {
                throw error;
            });
        }


    // Chercher utilisateur dans la base de donnees a partir de pseudonyme
    async getUserbyPseudo(pseudo:string): Promise <User> {
            const filter: Filter<User> = {pseudonyme: pseudo };
            return  this.UserCollection.findOne(filter)
                .then((user: User) => {
                    this.ExistingUserData = user;
                    return user;
                })
                .catch((error: Error) => {
                    throw error;
                });

        }      
        

    // Chercher utilisateur a partir du pseudo et faire une mise a jour des donnees
    async modifyUserData(data: User): Promise<void> {
            const filter: Filter<User> = {pseudonyme: data.pseudonyme};
            if (this.validateUserdata(data)) {
                this.UserCollection.findOneAndUpdate(filter,data).catch((error: Error) => {
                     throw error;
                 });
            } else {
                    alert('Invalide user data');
                }
            }

// Fonction verifiant le pseudonyme et le mot de passe    
    async  ValidateUserlogin (userdata: string[]) : Promise<string[]> {
        this.getUserbyPseudo(userdata[0]);
        if (this.ValidateUniquePseudo (userdata[0]) && (userdata[1] == this.ExistingUserData.password) ){    
            this.logginCredential[0]=userdata[0];
            this.logginCredential[1]=userdata[1];                   
        }
        else{
           alert("Le pseudonyme ou le mot de passe est erronné.")
        }
        return this.logginCredential;
    }  

// second verification server side 
    private validateUserdata(data: User): boolean {
        return  this.validateDataLenght(data) && 
                this.validateEmail(data.email)  && 
                this.ValidateUniquePseudo(data.pseudonyme);
    }
 // verification email    
    private validateEmail(email: string ): boolean {
            const regularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return regularExpression.test(String(email).toLowerCase());
    }

 // Validation de la longueur des champs minimal et maximal   
    private validateDataLenght(userdata : User ): boolean {
        if ((userdata.firstName.length > 0 && userdata.firstName.length <=15) &&
            (userdata.lastName.length > 0 && userdata.lastName.length <=15) &&
            (userdata.pseudonyme.length > 0 && userdata.pseudonyme.length <=15) &&
            (userdata.email.length > 0 && userdata.email.length <=15) &&
            (userdata.password.length > 0 && userdata.password.length <=15))
            {
                this.UserDataLenght = true;
            }
        else {
                this.UserDataLenght = false;
       }
       return this.UserDataLenght;

    }

   // validation pseudo existant dans la base de donnees 
    private ValidateUniquePseudo(pseudo: string ): boolean {
        if (this.ExistingUserData.pseudonyme.length == 1)
        {
            this.UniquePseudo = true;
        }
        else{
            this.UniquePseudo = false;
        }
                return this.UniquePseudo ;
    } 
  
}
