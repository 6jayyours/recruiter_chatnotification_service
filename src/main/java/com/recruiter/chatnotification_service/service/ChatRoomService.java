package com.recruiter.chatnotification_service.service;

import com.recruiter.chatnotification_service.model.ChatRoom;
import com.recruiter.chatnotification_service.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public Optional<String> getChatRoomId(
            Integer senderId,
            Integer receiverId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, receiverId);
                        return Optional.of(chatId);
                    }

                    return  Optional.empty();
                });
    }

    private String createChatId(Integer senderId, Integer receiverId) {
        var chatId = String.format("%s_%s", senderId, receiverId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
