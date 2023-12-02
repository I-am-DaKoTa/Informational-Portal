package ru.croc.informationalportal.model;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private User sender;
    private String messageText;
    private Timestamp sentTime;

    public Message() {
    }

    public Message(User sender, String messageText, Timestamp sentTime) {
        this.sender = sender;
        this.messageText = messageText;
        this.sentTime = sentTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public User getSender() {
        return sender;
    }

    public String getMessageText() {
        return messageText;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }
}