package com.nan.chatai.controller.app;

import ch.qos.logback.core.util.FileUtil;
import com.nan.chatai.entity.app.AppUser;
import com.nan.chatai.server.serverImpls.AppUserServiceImpl;
import com.nan.chatai.utils.RE;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("chat-ai")
public class FileUploadController {

    @Value("${wx-app.userAvatar}")
    private String userAvatarPath;

    @Autowired
    private AppUserServiceImpl appUserService;

    @PostMapping("/upload-avatar/{userId}")
    public RE uploadAvatar(@PathVariable("userId") String userId, @RequestParam("file") MultipartFile multipartFile){

        String avatarUrl="";
        try{

            String originalFilename = multipartFile.getOriginalFilename();
            String newFileName=userId+"."+originalFilename.split("\\.")[1];
            byte[] bytes = multipartFile.getBytes();

            //判断文件名是否重复，重复先将文件修改成ack
            File userAvatarFiles = new File(userAvatarPath);
            String backupOldAvatar="";
            if(userAvatarFiles.exists()){
                File[] userAvatars = userAvatarFiles.listFiles();
                for(File avatar: userAvatars){
                    if(avatar.getName().contains(userId)){
                        backupOldAvatar=userAvatarPath+"\\"+newFileName+".ack";
                        avatar.renameTo(new File(backupOldAvatar));
                    }
                }
            }else {
                userAvatarFiles.mkdirs();
            }

            saveAvatarFile(newFileName,bytes,userAvatarPath);
             avatarUrl="http://192.168.2.127:8070/app-images/avatar/"+newFileName;
            appUserService.changeUserAvatar(avatarUrl,userId);
            if(! "".equals(backupOldAvatar)) new File(backupOldAvatar).delete();

        }catch (IOException i){
            i.printStackTrace();
            return RE.Fail("修改头像失败");
        }
//        AppUser appUser = new AppUser();
//        appUser.setUserId(userId);
//        appUser.setAvatarUrl(avatarUrl);
        return RE.Success("修改头像成功",avatarUrl);
    }


    private void saveAvatarFile(String newFileName, byte[] fileByte, String savePath) throws IOException {

        //判断文件路径
        File avatarPath= new File(savePath);
        if(!avatarPath.exists()) avatarPath.mkdirs();

            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(savePath + "\\" + newFileName);
            fileOutputStream.write(fileByte);
            fileOutputStream.flush();
            fileOutputStream.close();

    }
}
