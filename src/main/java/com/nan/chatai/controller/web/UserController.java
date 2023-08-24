package com.nan.chatai.controller.web;

import com.nan.chatai.configs.AesEncryptConfig;
import com.nan.chatai.entity.web.ChatUser;
import com.nan.chatai.server.serverImpls.UserServerImpl;
import com.nan.chatai.utils.RE;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/chat-ai")
public class UserController {

    @Resource
    private UserServerImpl userServer;

    @Resource
    private AesEncryptConfig aesEncrypt;

    public static ConcurrentHashMap<String,ChatUser> Login_User=new ConcurrentHashMap<>();


    @RequestMapping("/")
    public String LoginPage(){
        return "user/login";
    }

    @RequestMapping("/register-page")
    public String RegisterPage(){
        return "user/register";
    }

    @PostMapping("/user-register")
    @ResponseBody
    public RE RegisterPage(@RequestBody ChatUser user){
        /*
        int test=1/0;
        int i = userServer.VerifyUserByPhone(user.getPhone());
        System.out.println("i: "+i);
         */

        try {
            if(userServer.VerifyUserByPhone(user.getPhone())>0) return RE.Invalid("手机号已注册");
            UUID uuid = UUID.randomUUID();
            user.setId(uuid.toString());
            user.setPassword(aesEncrypt.encrypt(user.getPassword()));
            userServer.AddUser(user);
        } catch (Exception exception) {
            exception.printStackTrace();
            return RE.Fail("服务器故障，请稍后再试");
        }
        return RE.Success("注册成功");
    }

    @PostMapping("/user-login")
    @ResponseBody
    public RE Login(@RequestBody ChatUser user,HttpServletRequest request){

        try {

            String encryptPassword=aesEncrypt.encrypt(user.getPassword());
            ChatUser userFromDB = userServer.SelectUserByNameAndPassword(user.getUsername(), encryptPassword);
          //  System.out.println("userFromDB: "+userFromDB.toString());

            if(null == userFromDB){
                return RE.Invalid("用户未注册或密码错误");
            }else {

               String userString= userFromDB.getId()+"-"+userFromDB.getUsername()+"-"+userFromDB.getPhone();
               String userToken=aesEncrypt.encrypt(userString);

                request.getSession().setAttribute("USER-CHAT-AI-ID",userToken);

                //request.getRequestDispatcher("/chat-ai/start").forward(request,response);
                Login_User.put(userFromDB.getId(),userFromDB);
               return RE.Success("登入成功！",userFromDB);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return RE.Fail("系统故障，请稍后再试");
        }
    }
}
