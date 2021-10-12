import { Repository } from '../../../domain/interfaces/repository.interface';
import { inject, injectable, unmanaged } from 'inversify';
import { Document, Model } from 'mongoose';
import { DbClient } from '../db_client';
import { TYPES } from '@app/domain/constants/types';

@injectable()
export abstract class GenericRepository<TModel extends Document>
	implements Repository<TModel>
{
	private name: string;
	private Model: Model<TModel>;
	private dbClient: DbClient;

	constructor(
		@inject(TYPES.DbClient) dbClient: DbClient,
		@unmanaged() name: string,
		schema: Model<TModel>,
	) {
		this.dbClient = dbClient;
		this.name = name;
		this.Model = schema;
	}
	save(doc: TModel): Promise<TModel> {
		throw new Error('Method not implemented.');
	}

	create(model: TModel): Promise<TModel> {
		return new Promise<TModel>((resolve, reject) => {
			this.Model.create(model, (error, data) => {
				if (error) {
					reject(error);
				}
				resolve(data);
			});
		});
	}

	public async findAll() {
		return new Promise<TModel[]>((resolve, reject) => {
			this.Model.find({}, (err, data: TModel[]) => {
				if (err) {
					reject(err);
				}
				resolve(data);
			});
		});
	}

	findById(id: string): Promise<TModel> {
		return new Promise<TModel>((resolve, reject) => {
			this.Model.findById(id, (err: Error, data: TModel) => {
				if (err) {
					reject(err);
				}
				resolve(data);
			});
		});
	}

	// https://masteringjs.io/tutorials/mongoose/update
	updateById(id: string, model: {}): Promise<TModel> {
		return new Promise<TModel>((resolve, reject) => {
			this.Model.findByIdAndUpdate(
				id,
				model,
				(err: any, data: TModel) => {
					if (err) {
						reject(err);
					}
					resolve(data);
				},
			);
		});
	}

	deleteById(id: string): Promise<TModel> {
		return new Promise<TModel>((resolve, reject) => {
			this.Model.findByIdAndDelete(id, (err: any, data: TModel) => {
				if (err) {
					reject(err);
				}
				resolve(data);
			});
		});
	}
}
