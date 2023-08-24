package com.nan.chatai.dao;

import com.nan.chatai.entity.app.ChatRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Mapper
public interface ChatRecordMapper {

    List<ChatRecord> selectUserChatRecord(@Param("userId") String userId);
}
