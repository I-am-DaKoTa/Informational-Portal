package ru.croc.informationalportal;

public class SearchUtils {
    String name;
    String surname;
    String patronymic;
    int age;
    String slogan;
    String program;
    String partyName;

    public SearchUtils(String name, String surname, String patronymic, int age, String slogan, String program, String partyName) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.age = age;
        this.slogan = slogan;
        this.program = program;
        this.partyName = partyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getAge() {
        return age;
    }

    public String getStringOfAge() {
        return String.valueOf(age);
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
