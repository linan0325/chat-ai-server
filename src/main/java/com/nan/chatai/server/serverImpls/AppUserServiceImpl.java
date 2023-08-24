package com.nan.chatai.server.serverImpls;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.nan.chatai.dao.AppUserMapper;
import com.nan.chatai.dao.mybatisPlus.AppUserMapperPlus;
import com.nan.chatai.entity.app.AppUser;
import com.nan.chatai.entity.app.VerifyCodeRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AppUserServiceImpl {

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private AppUserMapperPlus appUserMapperPlus;

    public void insertAppUser(AppUser appUser){
        appUserMapper.insertAppUser(appUser);
    }

    public int selectAppUserByPhone(String phoneNo){

        return appUserMapper.selectAppUserByPhone(phoneNo);
    }

    public AppUser selectAppUserByPhoneAndPassword(String phoneNo,String password){
        return appUserMapper.selectAppUserByPhoneAndPassword(phoneNo,password);
    }

    public int checkVerifyIfExist(String phone){
        return appUserMapper.checkVerifyIfExist(phone);
    }

    public int insertVerifyCode(VerifyCodeRecord verifyCodeRecord){
        return appUserMapper.insertVerifyCode(verifyCodeRecord);
    }

    public int verifyPhone(String phone){
        return appUserMapper.verifyPhone(phone);
    }

    public int changeUserAvatar(String avatarUrl,String userId){
        return appUserMapper.changeUserAvatar(avatarUrl,userId);
    }

    public int updateAppUserNameById(AppUser appUser){
        UpdateWrapper<AppUser> userUpdateWrapper =new UpdateWrapper<>();
        userUpdateWrapper.eq("USER_ID",appUser.getUserId()).set("USER_NAME",appUser.getUserName());
        return appUserMapperPlus.update(null,userUpdateWrapper);
    }

    public int changeUserPasswordByUserId(AppUser appUser){

        UpdateWrapper<AppUser> objectUpdateWrapper = new UpdateWrapper<>();
        objectUpdateWrapper.eq("USER_ID",appUser.getUserId()).set("PASSWORD",appUser.getPassword());

        return appUserMapperPlus.update(null,objectUpdateWrapper);

    }
}
