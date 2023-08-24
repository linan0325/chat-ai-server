package com.nan.chatai.server.serverImpls;

import com.nan.chatai.dao.UserMapper;
import com.nan.chatai.entity.ChatLog;
import com.nan.chatai.entity.web.ChatUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServerImpl{

    @Resource
    private UserMapper userMapper;


    public int AddUser(ChatUser user){
        return userMapper.AddUser(user);
    }

    public int VerifyUserByPhone(String phone){
        return userMapper.VerifyUserByPhone(phone);
    }

    public ChatUser SelectUserByNameAndPassword(String username,String password){

        return userMapper.SelectUserByNameAndPassword(username,password);
    }

    public void AddChatLog(ChatLog chatLog){
        userMapper.AddChatLog(chatLog);
    }
}
