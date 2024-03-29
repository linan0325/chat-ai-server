package com.nan.chatai.entity.gpt;

import lombok.Data;

@Data
public class Message {

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    private String role;
    private String content;
}
