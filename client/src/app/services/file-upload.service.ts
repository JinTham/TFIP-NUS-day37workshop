import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../models/upload-results';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private httpClient:HttpClient) { }

  getImage(postId:string){
    return firstValueFrom(this.httpClient.get<UploadResult>('/get-image/'+postId))
  }
}
