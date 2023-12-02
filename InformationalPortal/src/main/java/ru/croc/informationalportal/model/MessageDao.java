package ru.croc.informationalportal.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDao implements IMessageDao {
    private final Connection connection;
    public MessageDao(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Message createMessage(Message message) {
        String sql = "INSERT INTO public.message (sender_id, message_text, sent_time) VALUES (?, ?, ?) RETURNING message_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, message.getSender().getPhoneNumber());
            preparedStatement.setString(2, message.getMessageText());
            preparedStatement.setTimestamp(3, message.getSentTime());

            preparedStatement.execute();

            return message;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message findMessage(int message_id) {
        String sql = "SELECT * FROM public.message WHERE message_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, message_id);
            ResultSet userResult = preparedStatement.executeQuery();

            if (userResult.next()) {
                return new Message(new User(String.valueOf(userResult.getLong("phone_number")),
                        userResult.getString("name"), userResult.getString("surname"),
                        userResult.getString("patronymic"),
                        userResult.getDate("date_of_birth").toLocalDate(),
                        userResult.getString("password")), userResult.getString("message_text"),
                        userResult.getTimestamp("sent_time"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMessage(int message_id) {
        String deleteMessageSql = "DELETE FROM public.message WHERE message_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteMessageSql)) {

            preparedStatement.setLong(1, message_id);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
