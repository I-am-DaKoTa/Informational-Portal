package ru.croc.informationalportal.model;

import ru.croc.informationalportal.exceptions.InvalidUserDataException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User {
    private long phoneNumber;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate dateOfBirth;
    private String password;

    public User() {
    }

    public User(String phoneNumber, String name, String surname, String patronymic, LocalDate dateOfBirth, String password) {
        this.phoneNumber = phoneTrim(phoneNumber);
        if (isInvalidName(name)) {
            throw new InvalidUserDataException("Неверный формат имени");
        }
        if (isInvalidName(surname)) {
            throw new InvalidUserDataException("Неверный формат фамилии");
        }
        if (isInvalidPatronymic(patronymic)) {
            throw new InvalidUserDataException("Неверный формат отчества");
        }
        if (!isValidDateOfBirth(dateOfBirth)) {
            throw new InvalidUserDataException("Недействительная дата рождения");
        }
        if (!isUserAdult(dateOfBirth)) {
            throw new InvalidUserDataException("Пользователь должен быть старше 18 лет");
        }
        if (!isValidPassword (password)) {
            throw new InvalidUserDataException("Слабый парол");
        }

        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.password = hashPassword(password);
    }

    private String hashPassword(String password) {
        BCrypt.Hasher hasher = BCrypt.withDefaults();
        return hasher.hashToString(12, password.toCharArray());
    }

    public boolean verifyPassword(String enteredPassword, String hashedPassword) {
        BCrypt.Verifyer verifyer = BCrypt.verifyer();
        BCrypt.Result result = verifyer.verify(enteredPassword.toCharArray(), hashedPassword);
        return result.verified;
    }

    private long phoneTrim(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[- _()+]", "");

        if (phoneNumber.length() != 11) {
            throw new InvalidUserDataException("Некорректная длина номера телефона");
        }

        if (phoneNumber.startsWith("8") || phoneNumber.startsWith("7")) {
            phoneNumber = "7" + phoneNumber.substring(1);
        }

        return Long.parseLong(phoneNumber);
    }

    private boolean isInvalidName(String name) {
        return !name.matches("^[A-Za-zА-Яа-я]{2,32}$");
    }

    private boolean isInvalidPatronymic(String patronymic) {
        return patronymic != null && !patronymic.matches("^[A-Za-zА-Яа-я]{0,32}$");
    }

    private boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        LocalDate minDate = LocalDate.of(1923, 1, 1);
        LocalDate maxDate = LocalDate.of(2050, 1, 1);
        return !dateOfBirth.isBefore(minDate) && !dateOfBirth.isAfter(maxDate);
    }

    private boolean isUserAdult(LocalDate dateOfBirth) {
        LocalDate today = LocalDate.now();
        LocalDate adulthoodDate = today.minusYears(18);
        return !dateOfBirth.isAfter(adulthoodDate);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?!.*\\s)[ -~Ѐ-ӿ]{8,128}$";
        return password.matches(passwordPattern);
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassword() {
        return password;
    }
}
