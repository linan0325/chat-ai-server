package com.nan.chatai.entity.app;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("chat_log")
public class ChatRecord {

    @TableField("LOG_ID")
    private String logId;
    @TableField("USER_ID")
    private String userId;
    @TableField("SESSION_ID")
    private String sessionId;
    @TableField("USER_QUESTIONS")
    private String userQuestions;
    @TableField("AI_ANSWER")
    private String aiAnswer;
    @TableField("TIME")
    private String time;
}
