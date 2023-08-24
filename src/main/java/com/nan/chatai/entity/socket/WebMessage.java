package com.nan.chatai.entity.socket;

import lombok.Data;

@Data
public class WebMessage {
    private String text;
    private String type; /** new: 新建会话, check: socket连接检测  continue：继续会话*/
    private String sessionId;
}
