package com.example.newairbnb.request;

public class MessageUsers {
    private Long sender;
    private Long receiver;

    public MessageUsers() {
    }

    public MessageUsers(Long sender, Long receiver) {
        this.sender = sender;
        this.receiver = receiver;
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
}
