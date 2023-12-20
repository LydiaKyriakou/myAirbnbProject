import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessagesService } from 'src/app/services/messages.service';
import { AuthService } from 'src/app/auth/auth.service';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/model/user';
import { Message } from 'src/app/model/message';

@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css']
})
export class ConversationComponent implements OnInit {
  decodedToken: any;

  senderId?: number;
  receiverId?: number;

  senderUsername?: string;

  messageContent: string = '';

  messages: any[] = [];
  allMessages: Message[] = [];
  messagesToDisplay = 10;

  constructor(private route: ActivatedRoute, 
              private messagesService: MessagesService, 
              private authService: AuthService, 
              private userService: UserService
  ) {}

  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();
    const hostId = this.decodedToken.userId;

    this.route.params.subscribe(params => {
      this.senderId = +params['id'];
      this.receiverId = hostId;

      this.userService.getUserById(this.senderId).subscribe((sender: User) => {
        this.senderUsername = sender.username;
      });
      
      this.getConversation(this.senderId, hostId);
      this.loadMoreMessages();

    });
  }

  getConversation(senderId: number, receiverId: number) {
    this.messagesService.getConversation(senderId, receiverId).subscribe(data => {
      this.allMessages = data.reverse();
      
      this.messages = this.allMessages.slice(0, this.messagesToDisplay);
    });
  }

  sendMessage() {
    if (this.messageContent.trim() !== '') {
      const message = {
        sender: this.receiverId,
        receiver: this.senderId,
        content: this.messageContent
      };

      // Call your service to send the message
      this.messagesService.addMessage(message).subscribe(response => {
        console.log('Message sent:', response);

        // Clear the message input field after sending
        this.messageContent = '';
        window.location.reload();
      });
    }
  }

  deleteMessage(messageId: number) {
    this.messagesService.deleteMessage(messageId).subscribe(
      (response) => {
        // Handle success, e.g., remove the deleted message from your messages array
        this.messages = this.messages.filter(
          (message) => message.id !== messageId
        );
      },
      (error) => {
        // Handle error, e.g., show an error message
        console.error('Error deleting message:', error);
      }
    );
  }

  loadMoreMessages() {
    if (this.messagesToDisplay < this.allMessages.length) {
      const startIndex = this.messagesToDisplay;
      const endIndex = startIndex + 10; 
      const olderMessages = this.allMessages.slice(startIndex, endIndex);
      this.messagesToDisplay = endIndex;
      this.messages = [...this.messages, ...olderMessages.reverse()];
    }
  }
  
      
  convertTimestamp(timestampArray: number[] | undefined): Date | undefined {
    if (!timestampArray) {
      return undefined;
    }
  
    // Convert the array to a Date object
    return new Date(
      timestampArray[0], // Year
      timestampArray[1] - 1, // Month (0-based index)
      timestampArray[2], // Day
      timestampArray[3], // Hour
      timestampArray[4] // Minute
    );
  }
}

