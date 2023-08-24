package com.nan.chatai.controller.app;

import com.nan.chatai.entity.app.ChatRecord;
import com.nan.chatai.entity.gpt.Message;
import com.nan.chatai.server.serverImpls.UserChatRecordServiceImpl;
import com.nan.chatai.utils.RE;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chat-ai")
public class ChatRecordController {

    @Resource
    private UserChatRecordServiceImpl userChatRecordService;

    @GetMapping("/chat-record/{userId}")
    public RE selectUserChatRecord(@PathVariable("userId")String userId){

        if(null==userId || "".equals(userId)) return RE.Invalid("无请求参数用户ID");
        List<ChatRecord> chatRecords;
        try{
             chatRecords =
                    userChatRecordService.selectUserChatRecord(userId);
        }catch (Exception e){
            e.printStackTrace();

            return RE.Fail("服务器异常，请稍后再试");
        }
        return RE.Success("请求成功",chatRecords);

 }

    @PostMapping("del-record/{sessionId}")
    public RE delChatRecord(@PathVariable("sessionId")String sessionId){
        try {
            int i = userChatRecordService.delChatRecord(sessionId);
            System.out.println("i:"+i);
        }catch (Exception e){
            e.printStackTrace();
            return RE.Fail("系统故障，稍后再试");
        }
        return RE.Success("删除成功");
    }

    @GetMapping("get-records/{sessionId}")
    public RE selectChatRecordBySession(@PathVariable("sessionId") String sessionId){

        List<Message> msgList;
        try {
            msgList=userChatRecordService.selectChatRecordBySessionId(sessionId);
        }catch (Exception e){
            e.printStackTrace();
            return RE.Fail("系统故障");
        }
        return RE.Success("success",msgList);
    }
}
