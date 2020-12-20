package ru.sstu.shopik.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserChangeForm {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{1,15}")
    private String login;

    @Pattern(regexp = "[a-zA-Zа-яА-Я]{0,20}")
    private String firstName;

    @Pattern(regexp = "[a-zA-Zа-яА-Я]{0,20}")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z]{0,20}")
    private String role;

    @NotBlank
    @Pattern(regexp = "[0-9]{1,9}")
    private String balance;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
