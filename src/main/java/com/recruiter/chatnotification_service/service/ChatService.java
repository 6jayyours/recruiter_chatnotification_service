package com.recruiter.chatnotification_service.service;

import com.recruiter.chatnotification_service.model.Message;
import com.recruiter.chatnotification_service.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {


    private final ChatRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatService(ChatRepository repository, ChatRoomService chatRoomService) {
        this.repository = repository;
        this.chatRoomService = chatRoomService;
    }

    public Message save(Message chatMessage) {
        System.out.println(chatMessage);
        String chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<Message> findChatMessages(int senderId, int receiverId) {
        var chatId = chatRoomService.getChatRoomId(senderId, receiverId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }

    public String deleteMessageById(int id) {
        boolean exists = repository.existsById(id);
        if (!exists) {
            return "Chat doesnt exist";
        }
        repository.deleteById(id);
        return "Chat deleted successfully";
    }

    public String deleteSelfMessageById(int id) {
        Optional<Message> messageOptional = repository.findById(id);
        if (!messageOptional.isPresent()) {
            return "Chat doesn't exist";
        }
        Message message = messageOptional.get();
        message.setSelfDeleted(true);
        repository.save(message);
        return "Chat deleted successfully";
    }




    public List<Integer> findDistinctReceiverIdsBySenderId(Integer senderId) {
        return repository.findDistinctReceiverIdsBySenderId(senderId);
    }
}
