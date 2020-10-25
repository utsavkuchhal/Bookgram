package com.utsav.bookgram.Model;

public class ChatModel {

    private String sender;
    private String message;
    private String reciever;

    public String getSender() {
        return sender;
    }

    public ChatModel() {
    }

    public String getMessage() {
        return message;
    }

    public ChatModel(String sender, String message, String reciever) {
        this.sender = sender;
        this.message = message;
        this.reciever = reciever;
    }

    public String getReciever() {
        return reciever;
    }
}
