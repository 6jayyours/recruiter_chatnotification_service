package com.recruiter.chatnotification_service.dto;

import lombok.Data;

@Data
public class NotificationMessage {
    private String email;
    private String subject;
    private String content;

}
