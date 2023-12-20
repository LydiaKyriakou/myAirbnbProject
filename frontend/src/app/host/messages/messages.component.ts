import { Component, OnInit } from '@angular/core';
import { MessagesService } from 'src/app/services/messages.service';
import { AuthService } from 'src/app/auth/auth.service';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user.service';
import { Observable } from 'rxjs';
import { forkJoin } from 'rxjs';


@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {
  receiverIds: number[] = [];
  receivers: User[] = [];

  decodedToken: any;

  constructor(private messagesService: MessagesService, private authService: AuthService, private userService: UserService) { }

  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();
    const userId = this.decodedToken.userId;

    // Call the service function to get receiver IDs
    this.messagesService.getAllReceiversByUserId(userId).subscribe(
      (receiverIds: number[]) => {
        // Assign the received receiver IDs to your class variable
        this.receiverIds = receiverIds;
        console.log("this.receiverIds", this.receiverIds)
        this.loadReceiversData();
      },
      (error) => {
        // Handle any errors here
        console.error('Error fetching receiver IDs:', error);
      }
    );
  }

  loadReceiversData() {
    const requests: Observable<User>[] = [];

    for (const userId of this.receiverIds) {
      requests.push(this.userService.getUserById(userId));
    }

    // Use forkJoin to execute all requests in parallel
    forkJoin(requests).subscribe((receivers: User[]) => {
      this.receivers = receivers;
    });
  }
}
