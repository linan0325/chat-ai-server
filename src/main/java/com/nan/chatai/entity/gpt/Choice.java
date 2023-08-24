package com.nan.chatai.entity.gpt;

import lombok.Data;

@Data
public class Choice {
    private Delta delta;
   // private String delta;
    private String index;
    private String FinishReason;
}
