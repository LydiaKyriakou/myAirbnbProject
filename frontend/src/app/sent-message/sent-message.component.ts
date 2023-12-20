import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Message } from '../model/message';
import { MessagesService } from '../services/messages.service';
import { UserService } from '../services/user.service';
import { User } from '../model/user';

@Component({
  selector: 'app-sent-message',
  templateUrl: './sent-message.component.html',
  styleUrls: ['./sent-message.component.css']
})
export class SentMessageComponent implements OnInit {
  hostId?: number;
  userId?: number;

  hostUsername?: string;

  messageContent: string = '';

  messages: any[] = [];
  allMessages: Message[] = [];
  messagesToDisplay = 10;
  

  constructor(private route: ActivatedRoute,
              private messagesService: MessagesService,
              private userService: UserService
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.hostId = params['host-id'];
      this.userId = params['user-id'];

      if (this.hostId && this.userId) {
        
        this.userService.getUserById(this.hostId).subscribe((sender: User) => {
          this.hostUsername = sender.username;
        });

        this.getConversation(this.userId, this.hostId);
        this.loadMoreMessages();
      }
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
        sender: this.userId,
        receiver: this.hostId,
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
