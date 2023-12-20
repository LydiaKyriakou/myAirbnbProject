import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; 
import { HttpClient } from "@angular/common/http";
import { HttpClientModule, HTTP_INTERCEPTORS  } from '@angular/common/http';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AuthInterceptorService } from './auth/auth-interceptor.service';
import { UserService } from "./services/user.service";
import { RentaldService } from './services/rental.service';

import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { LogInComponent } from './log-in/log-in.component';
import { ProfileComponent } from './profile/profile.component';
import { EditProfileComponent } from './profile/edit-profile/edit-profile.component';
import { AdminComponent } from './admin/admin.component';
import { UserDetailsComponent } from './admin/user-details/user-details.component';
import { RentalsComponent } from './rentals/rentals.component';
import { RentalDetailsComponent } from './rentals/rental-details/rental-details.component';
import { EditRentalComponent } from './host/edit-rental/edit-rental.component';
import { HostComponent } from './host/host.component';
import { AddRentalComponent } from './host/add-rental/add-rental.component';
import { MakeReviewComponent } from './rentals/make-review/make-review.component';
import { MessagesComponent } from './host/messages/messages.component';
import { ConversationComponent } from './host/conversation/conversation.component';
import { AccessDeniedComponent } from './access-denied/access-denied.component';
import { SentMessageComponent } from './sent-message/sent-message.component';
import { ExportDataComponent } from './admin/export-data/export-data.component';
import { ReservationsComponent } from './reservations/reservations.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    RegisterComponent,
    LogInComponent,
    HomeComponent,
    ProfileComponent,
    EditProfileComponent,
    AdminComponent,
    UserDetailsComponent,
    RentalsComponent,
    RentalDetailsComponent,
    EditRentalComponent,
    HostComponent,
    AddRentalComponent,
    MakeReviewComponent,
    MessagesComponent,
    ConversationComponent,
    AccessDeniedComponent,
    SentMessageComponent,
    ExportDataComponent,
    ReservationsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule, 
    HttpClientModule,
    LeafletModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
  ],
  providers: [UserService, RentaldService, HttpClient, {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptorService,
    multi: true, // Allow multiple interceptors
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
