package com.recruiter.chatnotification_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CallUserRequest {
    private String type;
    private CallUserData data;
}