import { injectable } from 'inversify';
import { Collection, MongoClient, Filter} from 'mongodb';
import 'reflect-metadata';
import * as CONSTANTS from '../../../client/src/app/constants';
import { User } from '../../../client/src/app/interfaces/interface';

const DATABASE_URL = 'mongodb+srv://team111:polymtl2020@cluster0.dnwf5.mongodb.net/myFirstDatabase?retryWrites=true&w=majority';
const DATABASE_NAME = 'ColorImage'; 
const DATABASE_COLLECTION : string[] = ["UserParameter","GroupParameter","Drawing","Follower","ChatChanel","InstantMessage"]

@injectable()
export class DatabaseService {

    // the alert and/or console.log will be delete when we finish coding the part that use them
    
    UserCollection: Collection<User>;

    constructor() {
        MongoClient.connect(DATABASE_URL)
            .then((client: MongoClient) => {
                this.UserCollection = client.db(DATABASE_NAME).collection(DATABASE_COLLECTION[0]);
                console.log('Connexion a la base de donnée reussie'); // to be replace after by windows alert
            })
            .catch(() => {
                console.error('Erreur de connexion. Impossible de se connecter a la base de donnée');// to be replace after by windows alert
                process.exit(1);
            });

    }
    // create user account
        async addUserData(data: User): Promise<void> {
            if (this.validatedrawingdata(data)) {
                this.UserCollection.insertOne(data).catch((error: Error) => {
                    throw error;
                });
            } else {
                alert('Invalide user data');
            }
        }
    // get all user parameter from database
        async getAll(): Promise<User[]> {
            return  this.UserCollection.find({}).toArray()
                    .then((USERPARAM: User[]) => {
                        return USERPARAM;
                    })
                    .catch((error: Error) => {
                        throw  error;
                    });
        }

    // delete user from database : delete account from user parameter
        async deleteDrawing(pseudo: string): Promise<void> {
            return this.UserCollection
                .findOneAndDelete({ name: pseudo })
            .then(() => {
                alert('dessin supprimer de la base de donnée');
            })
            .catch((error: Error) => {
                throw error;
            });
        }


    // get user search by pseudo in all document in the collection 
    // verification client side for matching pseudo and mdp with form control : loggin verification
    async getUserbyPseudo(pseudo:string): Promise <User[]> {
            const filter: Filter<User> = {pseudonyme: pseudo };
            return  this.UserCollection.find(filter).toArray()
                .then((user: User[]) => {
                    return user;
                })
                .catch((error: Error) => {
                    throw error;
                });

        }

    // Update user data : find user by pseudo and update all field modify
    async modifyUserData(data: User): Promise<void> {
            const filter: Filter<User> = {pseudonyme: data.pseudonyme};
            if (this.validatedrawingdata(data)) {
                this.UserCollection.findOneAndUpdate(filter,data).catch((error: Error) => {
                     throw error;
                 });
            } else {
                    alert('Invalide user data');
                }
            }

// second verification server side field not empty required
    private validatedrawingdata(data: User): boolean {
        return this.validatePseudonyme(data.pseudonyme) && 
               this.validateFirsName(data.firstName) && 
               this.validateLastName(data.lastName) && 
               this.validateEmail(data.email) ;
    }
    private validatePseudonyme(pseudo: string ): boolean {
        return pseudo !== CONSTANTS.EMPTY_STRING ;
    }
    private validateFirsName(firstname: string ): boolean {
        return firstname !== CONSTANTS.EMPTY_STRING  ;
    }
    private validateLastName(lastname: string ): boolean {
        return lastname !== CONSTANTS.EMPTY_STRING  ;
    }
    private validateEmail(email: string ): boolean {
        return email !== CONSTANTS.EMPTY_STRING  ;
    }
  
}
