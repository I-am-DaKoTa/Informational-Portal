package ru.croc.informationalportal.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.croc.informationalportal.AppUtils;
import ru.croc.informationalportal.database.DatabaseConnection;
import ru.croc.informationalportal.model.UserDao;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class LoginController implements Initializable {
    private static final String PHONE_NUMBER_ERROR_MESSAGE = "Номер телефона должен состоять из 11 арабских цифр. Первая цифра должна быть 7 или 8.";
    private static final String DO_NOT_HAVE_ACCOUNT_ERROR_MESSAGE = "Пользователь с таким номером телефона не существует. Пожалуйста, зарегистрируйтесь.";
    private static final String INVALID_PASSWORD_ERROR_MESSAGE = "Пользователь с таким номером телефона уже зарегистрирован. Пожалуйста, войдите в аккаунт.";
    public String name;
    public String surname;
    public String patronymic;

    @FXML
    private TextField phoneNumber;

    @FXML
    private PasswordField password;

    @FXML
    private Button buttonLogIn;

    @FXML
    private Button buttonMoveToReg;

    @FXML
    private Label phoneNumberError;

    @FXML
    private Label passwordError;

    @FXML
    private Label userDoNotHasAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonLogIn.setOnAction(actionEvent -> {
            boolean isValid = validateFields();
            if (isValid) {
                if (isUserHasAccount()) {
                    AppUtils.changeScene(actionEvent, "view/main-view.fxml", "Информационный портал",
                            name, surname, patronymic);
                }
            }
        });

        buttonMoveToReg.setOnAction(actionEvent -> AppUtils.changeScene(actionEvent, "view/registration-view.fxml", "Регистрация",
                null, null, null));
    }

    private boolean isUserHasAccount() {
        try (Connection connection = DatabaseConnection.getConnection("informational_portal_db", "postgres", "123123")) {
            UserDao userDao = new UserDao(connection);
            var result = true;
            var userPhoneNumber = phoneTrim(phoneNumber.getText());
            var currentUser = userDao.findUser(userPhoneNumber);
            if (currentUser != null) {
                name = currentUser.getName();
                surname = currentUser.getSurname();
                patronymic = currentUser.getPatronymic();
            } else if (currentUser.verifyPassword(password.getText(), currentUser.getPassword())){
                userDoNotHasAccount.setText(INVALID_PASSWORD_ERROR_MESSAGE);
                result = false;
            } else {
                userDoNotHasAccount.setText(DO_NOT_HAVE_ACCOUNT_ERROR_MESSAGE);
                result = false;
            }


            DatabaseConnection.closeConnection(connection);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private long phoneTrim(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[- _()+]", "");
        phoneNumber = "7" + phoneNumber.substring(1);
        return Long.parseLong(phoneNumber);
    }


    private boolean validateFields() {
        boolean result = true;
        result &= validateField(phoneNumber.getText(), this::isInvalidPhoneNumber, phoneNumberError);
        String userPassword = password.getText();
        List<String> passwordErrors = getPasswordValidationErrors(userPassword);
        if (!passwordErrors.isEmpty()) {
            passwordError.setText(String.join(". ", passwordErrors));
            result = false;
        } else {
            passwordError.setText("");
        }

        return result;
    }

    private <T> boolean validateField(T value, Predicate<T> validation, Label errorLabel) {
        if (validation.test(value)) {
            errorLabel.setText(LoginController.PHONE_NUMBER_ERROR_MESSAGE);
            return false;
        } else {
            errorLabel.setText("");
            return true;
        }
    }

    private boolean isInvalidPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[- _()+]", "");
        return !phoneNumber.matches("[78]\\d{10}");
    }

    private List<String> getPasswordValidationErrors(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < 8 || password.length() > 128) {
            errors.add("Пароль должен быть длиной от 8 до 128 символов");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("Пароль должен содержать хотя бы одну цифру");
        }
        if (!password.matches(".*[a-zа-я].*")) {
            errors.add("Пароль должен содержать хотя бы одну строчную букву");
        }
        if (!password.matches(".*[A-ZА-Я].*")) {
            errors.add("Пароль должен содержать хотя бы одну заглавную букву");
        }
        if (!password.matches("[\\p{IsAlphabetic}\\p{IsDigit}\\p{Punct}]*")) {
            errors.add("Пароль может содержать только буквы, цифры и знаки пунктуации");
        }

        return errors;
    }
}
