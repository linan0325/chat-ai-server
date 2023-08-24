package com.nan.chatai.server.serverImpls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nan.chatai.dao.ChatRecordMapper;
import com.nan.chatai.dao.mybatisPlus.ChatRecordMapperPlus;
import com.nan.chatai.entity.app.ChatRecord;
import com.nan.chatai.entity.gpt.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserChatRecordServiceImpl {

    @Resource
    private ChatRecordMapper chatRecordMapper;

    @Resource
    private ChatRecordMapperPlus chatRecordMapperPlus;

    public List<ChatRecord> selectUserChatRecord(String userId){
        List<ChatRecord> chatRecords = chatRecordMapper.selectUserChatRecord(userId);
        return chatRecords;
    }

    public int delChatRecord(String sessionId){
        HashMap<String, Object> delMap = new HashMap<>();
        delMap.put("SESSION_ID",sessionId);
        return chatRecordMapperPlus.deleteByMap(delMap);
    }

    public List<Message> selectChatRecordBySessionId(String sessionId){

        QueryWrapper<ChatRecord> chatRecordQueryWrapper = new QueryWrapper<>();
        chatRecordQueryWrapper.eq("SESSION_ID",sessionId);
        chatRecordQueryWrapper.orderByAsc("TIME");
        List<ChatRecord> chatRecords = chatRecordMapperPlus.selectList(chatRecordQueryWrapper);

        List<Message> messages = new ArrayList<>();
        chatRecords.forEach(m->{
            messages.add(new Message("user",m.getUserQuestions()));
            messages.add(new Message("assistant",m.getAiAnswer()));
        });

        return messages;
    }
}
