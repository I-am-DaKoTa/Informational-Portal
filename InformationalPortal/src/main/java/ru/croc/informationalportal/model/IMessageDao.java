package ru.croc.informationalportal.model;

public interface IMessageDao {
    Message createMessage(Message message);
    Message findMessage(int message_id);
    void deleteMessage(int message_id);
}
