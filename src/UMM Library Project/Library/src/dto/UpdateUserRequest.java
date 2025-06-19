package dto;

public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String major;

    public UpdateUserRequest(String fullName, String email, String major) {
        this.fullName = fullName;
        this.email = email;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
