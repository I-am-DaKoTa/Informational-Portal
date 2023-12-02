package ru.croc.informationalportal.model;

import java.sql.*;

public class CandidateDao implements ICandidateDao {
    private final Connection connection;
    public CandidateDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Candidate createCandidate(Candidate candidate) {
        try {
            UserDao userDAO = new UserDao(connection);
            User createdUser = userDAO.createUser(candidate.getUser());

            String sql = "INSERT INTO public.candidate (user_id, slogan, program, party_name) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, createdUser.getPhoneNumber());
                preparedStatement.setString(2, candidate.getSlogan());
                preparedStatement.setString(3, candidate.getProgram());
                preparedStatement.setString(4, candidate.getPartyName());

                preparedStatement.executeUpdate();
            }

            return candidate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Candidate findCandidate(long phoneNumber) {
        String sql = "SELECT * FROM public.candidate WHERE phone_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, phoneNumber);
            ResultSet userResult = preparedStatement.executeQuery();

            if (userResult.next()) {
                return new Candidate(new User(String.valueOf(userResult.getLong("phone_number")),
                        userResult.getString("name"), userResult.getString("surname"),
                        userResult.getString("patronymic"),
                        userResult.getDate("date_of_birth").toLocalDate(),
                        userResult.getString("password")), userResult.getString("slogan"),
                        userResult.getString("text"), userResult.getString("party_name"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCandidate(long phoneNumber) {
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
