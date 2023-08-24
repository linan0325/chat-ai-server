package com.nan.chatai.dao;

import com.nan.chatai.entity.app.AppUser;
import com.nan.chatai.entity.app.VerifyCodeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

//@Mapper
public interface AppUserMapper {

    int insertAppUser(@Param("appUser")AppUser appUser);

    int selectAppUserByPhone(@Param("phoneNo") String phoneNo);

    AppUser selectAppUserByPhoneAndPassword(@Param("phone") String phone,@Param("password") String password);

    int checkVerifyIfExist(@Param("phone") String phone);

    int insertVerifyCode(@Param("verifyCodeRecord")VerifyCodeRecord verifyCodeRecord);

    int verifyPhone(@Param("phone") String phone);

    int changeUserAvatar(@Param("avatarUrl") String avatarUrl,@Param("userId") String userId);


}
