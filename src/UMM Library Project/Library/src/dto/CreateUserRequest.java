package dto;

public class CreateUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String major;

    public CreateUserRequest(String fullName, String email, String password, String major) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.major = major;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
