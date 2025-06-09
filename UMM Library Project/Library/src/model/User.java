package model;

public class User {
    private String name;
    private String email;
    private String password;
    private String dateOfBirth;

    public User(String email, String password) {
    }

    // Constructor, getters, setters
    public String getName() {
        return name;
    }

    public void setName(String newEmail) {
        this.name = newEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String newDateOfBirth) {
        this.dateOfBirth = newDateOfBirth;
    }
}