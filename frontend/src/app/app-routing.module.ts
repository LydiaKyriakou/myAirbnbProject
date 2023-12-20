import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { LogInComponent } from './log-in/log-in.component';
import { ProfileComponent } from './profile/profile.component';
import { EditProfileComponent } from './profile/edit-profile/edit-profile.component';
import { AdminComponent } from './admin/admin.component';
import { ExportDataComponent } from './admin/export-data/export-data.component';
import { UserDetailsComponent } from './admin/user-details/user-details.component';
import { ReservationsComponent } from './reservations/reservations.component';
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

const routes: Routes = [
  { path: '', component:HomeComponent },
  { path: 'register', component:RegisterComponent },
  { path: 'log-in', component:LogInComponent },
  { path: 'profile', component:ProfileComponent },
  { path: 'edit-profile', component:EditProfileComponent },
  { path: 'admin', component:AdminComponent },
  { path: 'export-data', component: ExportDataComponent },
  { path: 'user-details/:id', component: UserDetailsComponent },
  { path: 'reservations', component: ReservationsComponent },
  { path: 'rentals', component: RentalsComponent },
  { path: 'rentals/:id', component: RentalDetailsComponent },
  { path: 'edit-rental/:id', component: EditRentalComponent },
  { path: 'send-message/:host-id/:user-id', component: SentMessageComponent },
  { path: 'host', component: HostComponent },
  { path: 'add-rental', component: AddRentalComponent },
  { path: 'make-review/:id', component: MakeReviewComponent },
  { path: 'messages', component: MessagesComponent },
  { path: 'conversation/:id', component: ConversationComponent },
  { path: 'access-denied', component: AccessDeniedComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
