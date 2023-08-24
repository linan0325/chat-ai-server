package com.nan.chatai.entity.app;

import lombok.Data;

@Data
public class VerifyCodeRecord {
    private String Id;
    private String phone;
    private String verifyCode;
}
