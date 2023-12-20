package com.example.newairbnb.user;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long sender;
    private Long receiver;
    private String content;

    private LocalDateTime date;

    public Message(Long sender,Long receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return date;
    }
    public void setTimestamp(LocalDateTime localDateTime) {
        this.date = localDateTime;
    }


}
