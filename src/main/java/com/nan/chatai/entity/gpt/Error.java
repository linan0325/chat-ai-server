package com.nan.chatai.entity.gpt;

import lombok.Data;

@Data
public class Error {
    private String message;
    private String type;
    private String param;
    private int code;
}
