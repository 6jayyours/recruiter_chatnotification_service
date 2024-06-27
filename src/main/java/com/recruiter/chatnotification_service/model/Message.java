package com.recruiter.chatnotification_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;
import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String  chatId;
    private Date timestamp;

    @Lob
    @Column(name = "file_content", columnDefinition = "BLOB")
    private Blob fileContent;

    private String fileName;
    private String fileType;
    private String fileUrl;
    private boolean selfDeleted;
}
