package ru.croc.informationalportal.model;

public interface IUserDao {
    User createUser(User user);
    User findUser(long phoneNumber);
    void deleteUser(long phoneNumber);
}
