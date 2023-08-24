package com.nan.chatai.dao;

import com.nan.chatai.entity.web.ChatUser;
import com.nan.chatai.entity.ChatLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

//@Mapper
public interface UserMapper {

    int AddUser(@Param("chatUser") ChatUser chatUser);
    int VerifyUserByPhone(@Param("phone") String phone);
    ChatUser SelectUserByNameAndPassword(@Param("username") String username,@Param("password") String password);

    int AddChatLog(@Param("chatLog")ChatLog chatLog);
}
