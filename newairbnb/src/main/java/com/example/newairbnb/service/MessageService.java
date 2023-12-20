package com.example.newairbnb.service;

import com.example.newairbnb.repository.MessageRepository;
import com.example.newairbnb.user.Message;
import com.example.newairbnb.user.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getConversationByUsersId(Long user1, Long user2){
        List<Message> messages = messageRepository.findAll();
        List<Message> conversation = new ArrayList<>();

        for (Message message : messages) {
            if((message.getSender().equals(user1) && message.getReceiver().equals(user2))
                    || (message.getSender().equals(user2) && message.getReceiver().equals(user1)) ){
                conversation.add(message);
            }
        }
        return conversation;    }


    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

}
