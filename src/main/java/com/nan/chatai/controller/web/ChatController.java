package com.nan.chatai.controller.web;

import com.nan.chatai.entity.web.ChatUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/chat-ai")
public class ChatController {



    @GetMapping("/start/{userId}")
    public String StartChat(@PathVariable("userId") String userId, HttpServletRequest request){

        ChatUser chatUser = UserController.Login_User.get(userId);
        HttpSession session = request.getSession();
        String attribute = (String) session.getAttribute("USER-CHAT-AI-ID");
//       System.out.println("attribute:"+attribute);
//        System.out.println("chatUser:"+chatUser.toString());
        session.setAttribute("user-id",chatUser.getId());
        session.setAttribute("user-name",chatUser.getUsername());
        session.setAttribute("user-phone",chatUser.getPhone());
        session.setAttribute("user-head-sculpture",chatUser.getHeadSculpture());
       // System.out.println("chatUser: "+chatUser.toString());
        return "public/ChatAI";
    }



    @GetMapping("/user-head-sculpture")
    public String UserHeadSculpture(){

        return "/images/user-head-sculpture.jpg";
    }
}
