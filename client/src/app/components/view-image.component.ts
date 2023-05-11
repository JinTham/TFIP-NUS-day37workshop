import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FileUploadService } from '../services/file-upload.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-view-image',
  templateUrl: './view-image.component.html',
  styleUrls: ['./view-image.component.css']
})
export class ViewImageComponent implements OnInit, OnDestroy{
  
  postId= "";
    param$! : Subscription;
    imageData: any;

    constructor(private actRoute: ActivatedRoute, private fileUpSvc: FileUploadService){

    }

    ngOnInit(): void {
      this.actRoute.params.subscribe(
        async (params)=> {
          this.postId = params['postId'];
          // this.fileUpSvc.getImage(this.postId)
          //   .then((result)=>{
          //     this.imageData = result.image;
          //     console.log(this.imageData);
          // });
          let r = await this.fileUpSvc.getImage(this.postId);
          console.log(r.image);
          this.imageData = r.image;
        }
      );
    }

    ngOnDestroy(): void {
        this.param$.unsubscribe();
    }
}
