import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UploadResult } from '../models/upload-results';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CameraService {

  imageData = ""
  constructor(private httpClient: HttpClient) { }

  upload(form:any, image:Blob){
    const formData = new FormData()
    formData.set("title",form['title'])
    formData.set("complain",form['complain'])
    formData.set("file",image)

    return firstValueFrom(this.httpClient.post<UploadResult>("/upload",formData))
  }
}
