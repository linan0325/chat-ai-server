package com.nan.chatai.server;



import com.alibaba.fastjson.JSON;
import com.nan.chatai.dao.UserMapper;
import com.nan.chatai.entity.ChatLog;
import com.nan.chatai.entity.socket.WebMessage;
import com.nan.chatai.gpt.RequestGptAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
@ServerEndpoint("/ws/{userId}")
@Slf4j
public class WebSocketServer {

    /**
     * 存放每个客户端连接的用户信息
     */
    private static final Map<String,Session> userSessions=new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;


    private String userId = "";


    private static RequestGptAPI requestGptAPI;
    @Resource
    public void setRequestGptAPI(RequestGptAPI requestGptAPI){
        WebSocketServer.requestGptAPI=requestGptAPI;
    }

    private static UserMapper userMapper;
    @Resource
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    private String sessionID="";


    /**
     * 用户连接成功
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userID){

       // this.session=session;
        this.userId=userID;
        //this.sessionID=UUID.randomUUID().toString();
        userSessions.put(userID,session);
//        try {
//            sendMessage("连接成功",session);
//        }catch (IOException i){
//            i.printStackTrace();
//        }
        log.info("{}--open",userID);

    }

//    /**
//     *  接受客户端消息
//     * @param message
//     * @param session
//     */
//    @OnMessage
//    public void onMessage(String message,Session session){
//
//            if("check".equals(message)){
//                try {
//                    sendMessage("ok",session);
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
//
//            }else {
//
//                Consumer<String> consumer= res ->{
//                    try {
//                        sendMessage(res,session);
//                    } catch (IOException ioException) {
//                       try {
//                          session.close();
//                         } catch (IOException e) {
//                          e.printStackTrace();
//                         }
//                        ioException.printStackTrace();
//                    }
//                };
//
//                try{
//                    System.out.println("question:"+message);
//                    String fullAnswer = requestGptAPI.GetAnswerFromGpt(consumer,message,userId);
//                    ChatLog chatLog = new ChatLog();
//                    chatLog.setLogId(UUID.randomUUID().toString());
//                    chatLog.setUserId(this.userId);
//                    chatLog.setSessionId(this.sessionID);
//                    chatLog.setUserQuestions(message);
//                    chatLog.setChatAnswer(fullAnswer);
//
//                    userMapper.AddChatLog(chatLog);
//                }catch (Exception s){
//                    s.printStackTrace();
//                }
//
//            }
//    }

    /**
     *  接受客户端消息
     * @param messageJson
     * @param session
     */
    @OnMessage
    public void onMessage(String messageJson, Session session){

        WebMessage webMessage =JSON.parseObject(messageJson,WebMessage.class);

        log.info("{}---sessionId",webMessage.getSessionId());

        if("".equals(this.sessionID)){
            if("".equals(webMessage.getSessionId())
                    || null==webMessage.getSessionId() ||
                    "null".equals(webMessage.getSessionId())){
                this.sessionID=UUID.randomUUID().toString();
            }else {
                this.sessionID=webMessage.getSessionId();
            }
        }

        if("check".equals(webMessage.getType())){
            try {
                sendMessage("ok",session);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }else {

            Consumer<String> consumer= res ->{
                try {
                    sendMessage(res,session);
                } catch (IOException ioException) {
                    try {
                        session.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ioException.printStackTrace();
                }
            };

            try{
                //System.out.println("question:"+webMessage.getText());
                String fullAnswer = requestGptAPI.GetAnswerFromGpt(consumer,webMessage.getText(),userId,webMessage.getSessionId());
                fullAnswer=fullAnswer.replaceAll("null","");
                ChatLog chatLog = new ChatLog();
                chatLog.setLogId(UUID.randomUUID().toString());
                chatLog.setUserId(this.userId);
                chatLog.setSessionId(this.sessionID);
                chatLog.setUserQuestions(webMessage.getText());
                chatLog.setChatAnswer(fullAnswer);
                userMapper.AddChatLog(chatLog);
            }catch (Exception s){
                s.printStackTrace();
            }
        }
    }

    /**
     * 会话关闭
     * @param userID
     */
    @OnClose
    public void onClose(@PathParam("userId") String userID){

        userSessions.remove(userID);
        requestGptAPI.removeUserMsg(userID);
        this.sessionID="";
        log.info("{}--close",userID);
    }

//    @OnError
//    public void onError(Session session,Exception e){
//        e.printStackTrace();
//        System.out.println("连接错误");
//    }

    private void sendMessage(String message,Session session) throws IOException {
      session.getBasicRemote().sendText(message);
  }
}
