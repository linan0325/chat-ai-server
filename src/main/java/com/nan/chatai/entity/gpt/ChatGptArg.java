package com.nan.chatai.entity.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptArg {

    private String model="gpt-3.5-turbo";
    private boolean stream = true;
    //private double temperature=0.7;

    private List<Message> messages = new ArrayList<>();
    public void addMessages(Message message) {
        this.messages.add(message);
    }


}
