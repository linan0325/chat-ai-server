package com.nan.chatai.gpt;

import com.alibaba.fastjson.JSON;
import com.nan.chatai.entity.gpt.*;
import com.nan.chatai.entity.gpt.Message;
import com.nan.chatai.server.serverImpls.UserChatRecordServiceImpl;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Component
public class RequestGptAPI {

    @Value("${chat-ai.key}")
    private String key;
    @Value("${chat-ai.url}")
    private String url;
    @Resource(name = "httpAsyncClient")
    private CloseableHttpAsyncClient asyncClient;

    @Resource
    private UserChatRecordServiceImpl userChatRecordService;

    private final Charset charset = StandardCharsets.UTF_8;


    private Map<String, ChatGptArg> userMsg = new ConcurrentHashMap<>();


    public String GetAnswerFromGpt(Consumer<String> resConsumer,String questions,String userId){

        asyncClient.start();

         if(!userMsg.containsKey(userId)){
              ChatGptArg chatGptArg=new ChatGptArg();
              userMsg.put(userId,chatGptArg);
         }

         userMsg.get(userId).addMessages(new Message("user",questions));

        String ArgString= JSON.toJSONString(userMsg.get(userId));
       // System.out.println("ArgString:"+ArgString);
        AsyncRequestBuilder asyncRequest = asyncRequestBuilder(ArgString);

        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder answerRecord = new StringBuilder();

        AbstractCharResponseConsumer<HttpResponse> responseConsumer = new AbstractCharResponseConsumer<HttpResponse>(){
            HttpResponse httpResponse;

            @Override
            public void releaseResources() {}

            @Override
            protected int capacityIncrement() {
                return Integer.MAX_VALUE;
            }

            @Override
            protected void data(CharBuffer charBuffer, boolean b) throws IOException {
               //接收请求
                String AnswerString = charBuffer.toString();
                //System.out.println("AnswerString:"+AnswerString);
                if(!AnswerString.contains("data:") && AnswerString.contains("\"error\"")){
                    AnswerString=AnswerString.replaceAll("\\n","");
                    ChatGptError chatGptError =(ChatGptError) JSON.parseObject(AnswerString, ChatGptError.class);
                    answerRecord.append(chatGptError.getError().getMessage());//记录答案
                    resConsumer.accept(chatGptError.getError().getMessage());
                    return;
                }

                String[] datas = AnswerString.split("data:");

                    for(String data:datas){

                        AnswerString=data;

                        try {
                                AnswerData answerData= (AnswerData) JSON.parseObject(AnswerString,AnswerData.class);
                                String content = answerData.getChoices().get(0).getDelta().getContent();
                                 answerRecord.append(content);//记录答案
                                 resConsumer.accept(content);
//                                if(content != null){
//                                    answerRecord.append(content);//记录答案
//                                    resConsumer.accept(content);
//                                    //System.out.println("fullAnswer1:"+answerRecord.toString());
//                                }
                        }catch (Exception e){
                            // e.printStackTrace();
                            // System.out.println("AnswerString=error: "+AnswerString);
                        }
                    }
            }

            @Override
            protected void start(HttpResponse httpResponse, ContentType contentType) throws HttpException, IOException {
              setCharset(charset);
              this.httpResponse=httpResponse;
            }

            @Override
            protected HttpResponse buildResult() throws IOException {
                return httpResponse;
            }

        };


        Future<HttpResponse> Future = asyncClient.execute(asyncRequest.build(), responseConsumer, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse httpResponse) {
                latch.countDown();
                userMsg.get(userId).addMessages(new Message("assistant",answerRecord.toString()));
                System.out.println("回答结束");
            }

            @Override
            public void failed(Exception e) {
                e.printStackTrace();
                latch.countDown();
                //不将异常消息放入消息记录里面，会影响聊天记录
                List<Message> messages = userMsg.get(userId).getMessages();
                if(messages.size()>0 && messages.get(messages.size()-1).getRole()=="user"){
                    messages.remove(messages.size()-1);
                }
                answerRecord.append("系统故障,请稍后再试");
                resConsumer.accept("系统故障,请稍后再试");
            }

            @Override
            public void cancelled() {
                latch.countDown();
                answerRecord.append("请求取消");
                System.out.println("取消");
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        try {
//            asyncClient.close();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }

        return answerRecord.toString();
    }

    public String GetAnswerFromGpt(Consumer<String> resConsumer,String questions,String userId,String sessionId){

        asyncClient.start();

        if(!userMsg.containsKey(userId)){
            ChatGptArg chatGptArg=new ChatGptArg();
            if(!"".equals(sessionId)){
                //获取提问记录
                List<Message> messages = userChatRecordService.selectChatRecordBySessionId(sessionId);
                messages.forEach(message -> {
                    chatGptArg.getMessages().add(message);
                });
            }
            userMsg.put(userId,chatGptArg);
        }

        userMsg.get(userId).addMessages(new Message("user",questions));

        String ArgString= JSON.toJSONString(userMsg.get(userId));
         //System.out.println("ArgString:"+ArgString);
        AsyncRequestBuilder asyncRequest = asyncRequestBuilder(ArgString);

        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder answerRecord = new StringBuilder();

        AbstractCharResponseConsumer<HttpResponse> responseConsumer = new AbstractCharResponseConsumer<HttpResponse>(){
            HttpResponse httpResponse;

            @Override
            public void releaseResources() {}

            @Override
            protected int capacityIncrement() {
                return Integer.MAX_VALUE;
            }

            @Override
            protected void data(CharBuffer charBuffer, boolean b) throws IOException {
                //接收请求
                String AnswerString = charBuffer.toString();
                //System.out.println("AnswerString:"+AnswerString);
                if(!AnswerString.contains("data:") && AnswerString.contains("\"error\"")){
                    AnswerString=AnswerString.replaceAll("\\n","");
                    ChatGptError chatGptError =(ChatGptError) JSON.parseObject(AnswerString, ChatGptError.class);
                    answerRecord.append(chatGptError.getError().getMessage());//记录答案
                    resConsumer.accept(chatGptError.getError().getMessage());
                    return;
                }

                String[] datas = AnswerString.split("data:");

                for(String data:datas){

                    AnswerString=data;

                    try {
                        AnswerData answerData= (AnswerData) JSON.parseObject(AnswerString,AnswerData.class);
                        String content = answerData.getChoices().get(0).getDelta().getContent();
                        answerRecord.append(content);//记录答案
                        resConsumer.accept(content);
                    }catch (Exception e){
                        // e.printStackTrace();
                        // System.out.println("AnswerString=error: "+AnswerString);
                    }
                }
            }

            @Override
            protected void start(HttpResponse httpResponse, ContentType contentType) throws HttpException, IOException {
                setCharset(charset);
                this.httpResponse=httpResponse;
            }

            @Override
            protected HttpResponse buildResult() throws IOException {
                return httpResponse;
            }

        };


        Future<HttpResponse> Future = asyncClient.execute(asyncRequest.build(), responseConsumer, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse httpResponse) {
                latch.countDown();
                userMsg.get(userId).addMessages(new Message("assistant",answerRecord.toString()));
                System.out.println("回答结束");
            }

            @Override
            public void failed(Exception e) {
                e.printStackTrace();
                latch.countDown();
                //不将异常消息放入消息记录里面，会影响聊天记录
                List<Message> messages = userMsg.get(userId).getMessages();
                if(messages.size()>0 && messages.get(messages.size()-1).getRole()=="user"){
                    messages.remove(messages.size()-1);
                }
                answerRecord.append("系统故障,请稍后再试");
                resConsumer.accept("系统故障,请稍后再试");
            }

            @Override
            public void cancelled() {
                latch.countDown();
                answerRecord.append("请求取消");
                System.out.println("取消");
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        try {
//            asyncClient.close();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }

        return answerRecord.toString();
    }

    private AsyncRequestBuilder asyncRequestBuilder(String requestParameter){

        //创建一个请求
        AsyncRequestBuilder asyncRequest = AsyncRequestBuilder.post(url);

        ContentType contentType = ContentType.create("text/plain", charset);
        asyncRequest.setEntity(requestParameter,contentType);
        asyncRequest.setCharset(charset);

        // 设置请求头
        asyncRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        // 设置登录凭证
        asyncRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + key);

       return asyncRequest;

    }

    public void removeUserMsg(String userId){
        userMsg.remove(userId);
        System.out.println("移除 "+userId +" 的提问记录。。。。。");
    }

}
