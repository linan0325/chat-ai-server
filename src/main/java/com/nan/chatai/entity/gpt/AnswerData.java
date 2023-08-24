package com.nan.chatai.entity.gpt;

import com.nan.chatai.entity.gpt.Choice;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AnswerData {

    private String id;
    private String object;
    private String created;
    private String model;
    private ArrayList<Choice> choices;
}
