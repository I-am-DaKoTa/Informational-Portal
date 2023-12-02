package ru.croc.informationalportal.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.croc.informationalportal.AppUtils;
import ru.croc.informationalportal.database.DatabaseConnection;
import ru.croc.informationalportal.model.User;
import ru.croc.informationalportal.model.UserDao;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class RegistrationController implements Initializable {
    private static final String NAME_ERROR_MESSAGE = "Имя должно быть длиной от 2 до 32 символов и содержать только латинские или кириллические буквы";
    private static final String SURNAME_ERROR_MESSAGE = "Фамилия должна быть длиной от 2 до 32 символов и содержать только латинские или кириллические буквы";
    private static final String PATRONYMIC_ERROR_MESSAGE = "Отчество должно быть длиной от 2 до 32 символов и содержать только латинские или кириллические буквы";
    private static final String DATE_OF_BIRTH_ERROR_MESSAGE = "Для регистрации пользователю должно быть 18 лет.";
    private static final String PHONE_NUMBER_ERROR_MESSAGE = "Номер телефона должен состоять из 11 арабских цифр. Первая цифра должна быть 7 или 8.";
    private static final String ALREADY_HAVE_ACCOUNT_ERROR_MESSAGE = "Пользователь с таким номером телефона уже зарегистрирован. Пожалуйста, войдите в аккаунт.";

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField patronymic;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private TextField phoneNumber;

    @FXML
    private PasswordField password;

    @FXML
    private Button buttonReg;

    @FXML
    private Button buttonMoveToLogin;

    @FXML
    private Label nameError;

    @FXML
    private Label surnameError;

    @FXML
    private Label patronymicError;

    @FXML
    private Label dateOfBirthError;

    @FXML
    private Label phoneNumberError;

    @FXML
    private Label passwordError;

    @FXML
    private Label userHasAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonReg.setOnAction(actionEvent -> {
            boolean isValid = validateFields();

            if (isValid) {
                if (isUserHasAccount()) {
                    AppUtils.changeScene(actionEvent, "view/main-view.fxml", "Информационный портал",
                            name.getText(), surname.getText(), patronymic.getText());
                }
            }
        });

        buttonMoveToLogin.setOnAction(actionEvent -> AppUtils.changeScene(actionEvent, "view/login-view.fxml", "Аутентификация", null, null, null));
    }

    private boolean isUserHasAccount(){
        String enteredName = name.getText();
        String enteredSurname = surname.getText();
        String enteredPatronymic = patronymic.getText();
        LocalDate enteredDateOfBirth = dateOfBirth.getValue();
        String enteredPhoneNumber = phoneNumber.getText();
        String enteredPassword = password.getText();

        if (enteredName.isEmpty() || enteredSurname.isEmpty() || enteredPatronymic.isEmpty() ||
                enteredDateOfBirth == null || enteredPhoneNumber.isEmpty() || enteredPassword.isEmpty()) {
            return false;
        }
        try (Connection connection = DatabaseConnection.getConnection("informational_portal_db", "postgres", "123123")) {
            UserDao userDao = new UserDao(connection);
            User user = new User(phoneNumber.getText(), name.getText(), surname.getText(), patronymic.getText(),
                    dateOfBirth.getValue(), password.getText());
            var result = true;
            if (userDao.findUser(user.getPhoneNumber()) == null) {
                userDao.createUser(user);
            } else {
                userHasAccount.setText(ALREADY_HAVE_ACCOUNT_ERROR_MESSAGE);
                result = false;
            }
            DatabaseConnection.closeConnection(connection);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateFields() {
        boolean result = true;
        result &= validateField(name.getText(), NAME_ERROR_MESSAGE, this::isInvalidName, nameError);
        result &= validateField(surname.getText(), SURNAME_ERROR_MESSAGE, this::isInvalidName, surnameError);
        result &= validateField(patronymic.getText(), PATRONYMIC_ERROR_MESSAGE, this::isInvalidPatronymic, patronymicError);
        result &= validateField(dateOfBirth.getValue(), DATE_OF_BIRTH_ERROR_MESSAGE, this::isUserNotAdult, dateOfBirthError);
        result &= validateField(phoneNumber.getText(), PHONE_NUMBER_ERROR_MESSAGE, this::isInvalidPhoneNumber, phoneNumberError);
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

    private <T> boolean validateField(T value, String errorMessage, Predicate<T> validation, Label errorLabel) {
        if (validation.test(value)) {
            errorLabel.setText(errorMessage);
            return false;
        } else {
            errorLabel.setText("");
            return true;
        }
    }

    private boolean isInvalidName(String name) {
        return name == null || !name.matches("^[A-Za-zА-Яа-я]{2,32}$");
    }

    private boolean isInvalidPatronymic(String patronymic) {
        return patronymic == null || !patronymic.matches("^[A-Za-zА-Яа-я]{2,32}$");
    }

    private boolean isUserNotAdult(LocalDate dateOfBirth) {
        return dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now().minusYears(18));
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
