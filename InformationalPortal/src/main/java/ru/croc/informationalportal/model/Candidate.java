package ru.croc.informationalportal.model;

import ru.croc.informationalportal.exceptions.InvalidCandidateDataException;

public class Candidate {
    private int candidateId;
    private User user;
    private String slogan;
    private String program;
    private String partyName;

    public Candidate() {
    }

    public Candidate(User user, String slogan, String program, String partyName) {
        if (isValidSlogan(slogan)) {
            throw new InvalidCandidateDataException("Неверный формат имени");
        }
        if (isValidProgram(program)) {
            throw new InvalidCandidateDataException("Неверная длина программы");
        }
        if (!isValidPartyName(partyName)) {
            throw new InvalidCandidateDataException("Неверная длина названия партии");
        }

        this.user = user;
        this.slogan = slogan;
        this.program = program;
        this.partyName = partyName;
    }

    private boolean isValidSlogan(String slogan) {
        return slogan != null && !slogan.isEmpty() && slogan.length() <= 256;
    }

    private boolean isValidProgram(String program) {
        return program != null && !program.isEmpty() && program.length() <= 512;
    }

    private boolean isValidPartyName(String partyName) {
        return partyName != null && partyName.length() >= 2 && partyName.length() <= 32;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public String getSlogan() {
        return slogan;
    }

    public String getProgram() {
        return program;
    }

    public String getPartyName() {
        return partyName;
    }

    public User getUser() {
        return user;
    }
}