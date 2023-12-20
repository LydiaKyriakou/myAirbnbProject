import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../model/message';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {
  private baseUrl = 'https://localhost:8443/user/messages'; 

  constructor(private http: HttpClient) {}

  getAllReceiversByUserId(userId: number): Observable<number[]> {
    const url = `${this.baseUrl}/show/receiversfrom=${userId}`;
    console.log("url", url)
    return this.http.get<number[]>(url);
  }

  getConversation(senderId: number, receiverrId: number): Observable<any> {
    const url = `${this.baseUrl}/show/sender=${senderId}&receiver=${receiverrId}`;
    return this.http.get<any>(url);
  }

  addMessage(message: Message): Observable<Message> {
    const url = `${this.baseUrl}/add/sender=${message.sender}&receiver=${message.receiver}`;
    return this.http.post<Message>(url, message);
  }

  deleteMessage(messageId: number): Observable<any> {
    const url = `${this.baseUrl}/delete/${messageId}`;
    return this.http.delete(url);
  }
}
