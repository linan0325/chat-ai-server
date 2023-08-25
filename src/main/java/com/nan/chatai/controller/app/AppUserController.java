package com.nan.chatai.controller.app;

import com.nan.chatai.configs.AesEncryptConfig;
import com.nan.chatai.entity.app.AppUser;
import com.nan.chatai.entity.app.VerifyCodeRecord;
import com.nan.chatai.server.serverImpls.AppUserServiceImpl;
import com.nan.chatai.utils.RE;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
@RequestMapping("/chat-ai")
public class AppUserController {

    @Resource
    private AppUserServiceImpl appUserService;

    @Resource
    private AesEncryptConfig aesEncrypt;

    @PostMapping("/weChat-user-register")
    @ResponseBody
    public RE weChatRegister(@RequestBody AppUser appUser){
        try {

            String userPhoneNo=appUser.getPhoneNo();

            if(appUserService.selectAppUserByPhone(userPhoneNo)>0)return RE.Invalid("手机号已被注册");

            String userId = UUID.randomUUID().toString();
            appUser.setUserId(userId);

            appUser.setPassword(aesEncrypt.encrypt(appUser.getPassword()));
            appUserService.insertAppUser(appUser);
        } catch (Exception e) {
            e.printStackTrace();
            return RE.Fail("注册失败");
        }
             return RE.Success("注册成功");
    }

    @PostMapping("/weChat-user-login")
    @ResponseBody
    public RE weChatLogin(@RequestBody AppUser appUser){
        AppUser appUserFormDB=null;
        try {
            if("".equals(appUser.getVerifyCode()) && !"".equals(appUser.getPassword())){

                String encryptPassword = aesEncrypt.encrypt(appUser.getPassword());
                appUserFormDB = appUserService.selectAppUserByPhoneAndPassword(appUser.getPhoneNo(), encryptPassword);

                if(appUserFormDB ==null){
                    return RE.Fail("用户名或密码错误");
                }

                //默认用户头像
                if(appUserFormDB.getAvatarUrl()==null || appUserFormDB.getAvatarUrl().equals("")){
                    appUserFormDB.setAvatarUrl("http://192.168.2.127:8070/app-images/my/user.png");
                }

            }else {
                return RE.Fail("参数错误");
            }
        }catch (Exception e){
           e.printStackTrace();
           return RE.Fail("系统故障");
        }
        return RE.Success("登入成功",appUserFormDB);
    }

    @PostMapping("/weChat-user-logon")
    @ResponseBody
   // @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public RE weChatLogon(@RequestBody AppUser appUser){

      //  System.out.println("appUser:"+appUser.toString());

        if(appUser==null){
            return RE.Fail("参数错误");
        }

        String phoneNO=appUser.getPhoneNo();
        String password=appUser.getPassword();

        try{
            if(appUserService.verifyPhone(phoneNO)>0){
                return RE.Fail("该手机已注册");
            }
            appUser.setUserId(UUID.randomUUID().toString());
            appUser.setPassword(aesEncrypt.encrypt(password));
            appUser.setUserName("普通用户");//默认用户名
            appUser.setAvatarUrl("http://192.168.2.127:8070/app-images/my/user.png");//默认头像
            appUserService.insertAppUser(appUser);
            //return RE.Success("注册成功");
        }catch (Exception e){
            e.printStackTrace();
            return RE.Fail("注册失败系统故障");
        }
        return RE.Success("注册成功");
    }




    @GetMapping("/user-get-verify-code")
    @ResponseBody
    public RE SendVerifyCode(@RequestBody String phone){

        try{

            if(appUserService.checkVerifyIfExist(phone)>0){
                return RE.Invalid("请勿重复发送");
            }

            String verificationCode = String.valueOf((int)((Math.random()*9+1)*1000));
            VerifyCodeRecord verifyCodeRecord = new VerifyCodeRecord();
            verifyCodeRecord.setId(UUID.randomUUID().toString());
            verifyCodeRecord.setPhone(phone);
            verifyCodeRecord.setVerifyCode(verificationCode);

            appUserService.insertVerifyCode(verifyCodeRecord);

            return RE.Success("发送成功",verifyCodeRecord);

        }catch (Exception e){
            e.printStackTrace();
            return RE.Fail("系统故障,发送失败");
        }
    }

    @PostMapping("/update-app-user-username")
    @ResponseBody
    public RE updateUserNameById(@RequestBody AppUser appUser){

        try{
            appUserService.updateAppUserNameById(appUser);
        }catch (Exception e){
            e.printStackTrace();
            return RE.Fail("修改失败");
        }

        return RE.Success("修改成功");
    }

    @PostMapping("/change-user-password")
    @ResponseBody
    public RE changeUserPasswordByUserId(@RequestBody AppUser appUser){
        try{
            appUser.setPassword(aesEncrypt.encrypt(appUser.getPassword()));
            appUserService.changeUserPasswordByUserId(appUser);
        }catch (Exception e){
            e.printStackTrace();
            return  RE.Fail("修改失败");
        }
        return RE.Success("修改成功");
    }
}
