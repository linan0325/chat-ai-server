package com.nan.chatai.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RE {

    private int code;
    private String message;
    private Object object;

    public static RE Success(){
        return new RE(200,"success",null);
    }
    public static RE Success(String message){
        return new RE(200,message,null);
    }
    public static RE Success(String message,Object object){
        return new RE(200,message,object);
    }

    public static RE Fail(){
        return new RE(500,"fail",null);
    }
    public static RE Fail(String message){
        return new RE(500,message,null);
    }
    public static RE Fail(String message,Object object){
        return new RE(500,message,object);
    }
    public static RE Invalid(){
        return new RE(403,"invalid",null);
    }
    public static RE Invalid(String message){
        return new RE(403,message,null);
    }
    public static RE Invalid(String message,Object object){
        return new RE(403,message,object);
    }
}