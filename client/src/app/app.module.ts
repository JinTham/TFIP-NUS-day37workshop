import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WebcamModule } from 'ngx-webcam';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CameraComponent } from './components/camera.component';
import { UploadComponent } from './components/upload.component'
import { MaterialModule } from './material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ViewImageComponent } from './components/view-image.component';

@NgModule({
  declarations: [
    AppComponent,
    CameraComponent,
    UploadComponent,
    ViewImageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    WebcamModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterialModule,
    BrowserAnimationsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
