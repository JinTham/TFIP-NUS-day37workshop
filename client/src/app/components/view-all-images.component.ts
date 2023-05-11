import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { FileUploadService } from '../services/file-upload.service';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

@Component({
  selector: 'app-view-all-images',
  templateUrl: './view-all-images.component.html',
  styleUrls: ['./view-all-images.component.css']
})
export class ViewAllImagesComponent {

  param$! : Subscription
  images!: any
  imageList:any[] = []
  image!:any
  image1:any
  image2:any
  image3:any
  image4:any

  constructor(private actRoute: ActivatedRoute, private fileUpSvc: FileUploadService){ }

  ngOnInit(): void {
    this.param$ = this.actRoute.params.subscribe(
      async (params)=> {
        this.images = await this.fileUpSvc.getAllImagesFromS3();
        console.log(this.images)
        for (let i=0;i<4;i++){
          this.image = await this.fileUpSvc.downloadImageFromS3(this.images[i])
          this.imageList.push(this.image.image)
          console.log(this.imageList)
        }
        this.image1 = this.imageList[0]
        this.image2 = this.imageList[1]
        this.image3 = this.imageList[2]
        this.image4 = this.imageList[3]
      }
    );
  }

  ngOnDestroy(): void {
      this.param$.unsubscribe();
  }
  
}

