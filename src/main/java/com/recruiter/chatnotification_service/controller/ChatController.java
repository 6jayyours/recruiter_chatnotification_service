package com.recruiter.chatnotification_service.controller;


import com.recruiter.chatnotification_service.model.CallUserRequest;
import com.recruiter.chatnotification_service.model.ChatNotification;
import com.recruiter.chatnotification_service.model.Message;
import com.recruiter.chatnotification_service.service.ChatService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat")
    public ChatNotification processMessage(@Payload Message chatMessage) {
        Message savedMsg = chatService.save(chatMessage);
        ChatNotification notification=new ChatNotification(
                savedMsg.getId(),
                savedMsg.getSenderId().toString(),
                savedMsg.getReceiverId(),
                savedMsg.getContent());
        savedMsg.getTimestamp();
        messagingTemplate.convertAndSendToUser(
                String.valueOf(savedMsg.getReceiverId()), "/queue/messages", notification);
        return notification;
    }

    @GetMapping("/api/ws/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> findChatMessages(@PathVariable int senderId,
                                                          @PathVariable int receiverId) {
        return ResponseEntity
                .ok(chatService.findChatMessages(senderId, receiverId));
    }

    @GetMapping("/ws/files/{filename}")
    public ResponseEntity<byte[]> findChatFiles(@PathVariable String filename) throws IOException {
        Path imagePath = Paths.get(CHAT_UPLOAD_DIR).resolve(filename);
        if (!Files.exists(imagePath)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] imageBytes = Files.readAllBytes(imagePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // Adjust if needed
        headers.setContentLength(imageBytes.length);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/receivers/{senderId}")
    public ResponseEntity<List<Integer>> findDistinctReceiverIdsBySenderId(@PathVariable Integer senderId) {
        List<Integer> receiverIds = chatService.findDistinctReceiverIdsBySenderId(senderId);
        return ResponseEntity.ok(receiverIds);
    }

    @MessageMapping("/videocall")
    public void requestVideoCall(@Payload CallUserRequest request){
        messagingTemplate.convertAndSendToUser(
                request.getData().getUserToCall(),"/queue/messages", request
        );
    }

    private static final String CHAT_UPLOAD_DIR = "company/src/main/resources/static/chat-files";

    @PostMapping("/uploadChatFile")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        Path savedFilePath = saveFile(file);
        String fileUrl = savedFilePath.toString();
        System.out.println("File uploaded successfully. File URL: " + fileUrl);
        return ResponseEntity.ok(fileUrl);
    }


    private Path saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No file was uploaded.");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadDir = Paths.get(CHAT_UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path filePath = uploadDir.resolve(fileName);

        try {
            System.out.println("Attempting to save file to: " + filePath.toAbsolutePath());
            Files.write(filePath, file.getBytes());
            System.out.println("File saved successfully: " + filePath.toAbsolutePath());
            return filePath;
        } catch (IOException e) {
            System.err.println("Failed to save file: " + e.getMessage());
            throw e;
        }
    }


    @DeleteMapping("/deletechat/{id}")
    public ResponseEntity<String> deleteMessageById(@PathVariable int id) {
        return ResponseEntity.ok(chatService.deleteMessageById(id));
    }

    @PutMapping("/deletechatSelf/{id}")
    public ResponseEntity<String> deleteSelfMessageById(@PathVariable int id) {
        return ResponseEntity.ok(chatService.deleteSelfMessageById(id));
    }




}
