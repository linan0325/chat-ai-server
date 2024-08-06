package com.nan.chatai;


import com.nan.chatai.gpt.RequestGptAPI;
import com.nan.chatai.server.serverImpls.UserChatRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
class ChatAiServerApplicationTests {

    @Resource
    private RequestGptAPI requestGptAPI;

    @Resource
    private UserChatRecordServiceImpl userChatRecordService;

    @Test
    void contextLoads() {
        //29fb7f52-a6be-4a48-a843-32f936dbedd8
       // userChatRecordService.selectChatRecordBySessionId("29fb7f52-a6be-4a48-a843-32f936dbedd8");
        requestGptAPI.GetAnswerFromGpt(System.out::print,"你好","863754fd-2c41-4a65-9dcd-da03d7ca3819");

    }

}
