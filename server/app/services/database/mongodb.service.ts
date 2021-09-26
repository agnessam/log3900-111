import { injectable } from 'inversify';
import { MongoClient } from 'mongodb';

@injectable()
export class MongoService {
	private uri: string;
	private mongoClient: MongoClient;

	constructor() {
		this.start();
	}

	private async start(): Promise<void> {
		try {
			const client = new MongoClient(this.uri);
			this.mongoClient = await client.connect();
		} catch (error) {
			throw new Error('Database connection error: ' + error).message;
		}
	}

	private async closeConnection(): Promise<void> {
		return this.mongoClient.close();
	}
}
