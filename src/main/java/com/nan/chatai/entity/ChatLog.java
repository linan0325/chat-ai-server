package com.nan.chatai.entity;

import lombok.Data;

@Data
public class ChatLog {

    private String logId;
    private String userId;
    private String userQuestions;
    private String chatAnswer;
    private String times;
    private String sessionId;

}
