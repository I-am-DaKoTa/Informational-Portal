package ru.croc.informationalportal.model;

import java.sql.*;

public class UserDao implements IUserDao {
    private final Connection connection;
    public UserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO public.\"user\"" +
                "(phone_number, name, surname, patronymic, date_of_birth, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, user.getPhoneNumber());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getPatronymic());
            preparedStatement.setDate(5, Date.valueOf(user.getDateOfBirth()));
            preparedStatement.setString(6, user.getPassword());

            preparedStatement.execute();

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUser(long phoneNumber) {
        String sql = "SELECT * FROM public.\"user\" WHERE phone_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, phoneNumber);
            ResultSet userResult = preparedStatement.executeQuery();

            if (userResult.next()) {
                return new User(String.valueOf(userResult.getLong("phone_number")), userResult.getString("name"),
                        userResult.getString("surname"), userResult.getString("patronymic"),
                        userResult.getDate("date_of_birth").toLocalDate(), userResult.getString("password"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(long phoneNumber) {
        String deleteCandidateSql = "DELETE FROM public.candidate WHERE phone_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCandidateSql)) {

            preparedStatement.setLong(1, phoneNumber);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String deleteMessageSql = "DELETE FROM public.message WHERE sender_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteMessageSql)) {

            preparedStatement.setLong(1, phoneNumber);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String deleteUserSql = "DELETE FROM public.\"user\" WHERE phone_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUserSql)) {

            preparedStatement.setLong(1, phoneNumber);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
