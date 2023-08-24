package com.nan.chatai.entity.app;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("app_user")
public class AppUser {

    @TableField("USER_ID")
    private String userId;
    @TableField(value = "PHONE_NO")
    //@TableField(value = "PHONE_NO",updateStrategy = FieldStrategy.IGNORED)
    private String phoneNo;
    @TableField("PASSWORD")
    //@TableField(value = "PASSWORD",updateStrategy = FieldStrategy.IGNORED)
    private String password;
    @TableField("USER_NAME")
    //@TableField(value = "USER_NAME",updateStrategy = FieldStrategy.IGNORED)
    private String userName;
    @TableField("AVATAR_URL")
   // @TableField(value = "AVATAR_URL",updateStrategy = FieldStrategy.IGNORED)
    private String avatarUrl;
    private String verifyCode;
}
