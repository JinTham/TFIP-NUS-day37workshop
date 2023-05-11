import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CameraComponent } from './components/camera.component';
import { UploadComponent } from './components/upload.component';
import { ViewImageComponent } from './components/view-image.component';
import { ViewAllImagesComponent } from './components/view-all-images.component';

const routes: Routes = [
  {path:"", component: CameraComponent},
  {path: "upload", component: UploadComponent},
  {path: "image/:postId", component: ViewImageComponent},
  {path: "images", component: ViewAllImagesComponent},
  { path: "**", redirectTo: "/", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
