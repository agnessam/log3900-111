import { injectable } from 'inversify';
import { UploadServiceInterface } from '../interfaces/upload-service.interface';
import AWS from 'aws-sdk';

type S3UploadConfig = {
  accessKeyId?: string;
  accessKey?: string;
  destinationBucketName?: string;
  region?: string;
};

@injectable()
export class S3UploadService implements UploadServiceInterface {
  s3: AWS.S3;
  s3Config: S3UploadConfig;

  constructor() {
    this.s3Config = new AWS.Config({
      accessKeyId: process.env.AWS_ACCESS_KEY_ID,
      secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
      region: 'us-east-1',
    });
  }

  upload() {
    return true;
  }
}
