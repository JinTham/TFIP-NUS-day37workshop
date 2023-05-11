import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../models/upload-results';
import { firstValueFrom, lastValueFrom, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private httpClient:HttpClient) { }

  getImage(postId:string){
    return firstValueFrom(this.httpClient.get<UploadResult>('/get-image/'+postId))
  }

  getAllImagesFromS3():Promise<any>{
    return lastValueFrom(this.httpClient.get<UploadResult[]>('/get-all-images'))
  }

  downloadImageFromS3(imgName:string) {
    return lastValueFrom(this.httpClient.get<string>('/get-image-s3/'+imgName))
  }
}
