package com.nan.chatai.entity.web;

import lombok.Data;

@Data
public class ChatUser {
    private String id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String code;
    private String headSculpture;
}
