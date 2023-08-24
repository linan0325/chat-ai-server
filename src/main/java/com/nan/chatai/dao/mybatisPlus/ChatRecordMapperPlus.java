package com.nan.chatai.dao.mybatisPlus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nan.chatai.entity.app.ChatRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChatRecordMapperPlus extends BaseMapper<ChatRecord> {

//    @Select("select * from chat_log where SESSION_ID=#{sessionId}")
//    List<ChatRecord> selectChatRecordBySessionId(@Param("sessionId") String sessionId);
}
