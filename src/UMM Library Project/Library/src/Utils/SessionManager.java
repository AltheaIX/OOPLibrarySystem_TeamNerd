package Utils;

import model.User;

public class SessionManager {
    private static SessionManager instance;

    private User user;

    private SessionManager() {
        // private constructor to prevent instantiation
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // User getter & setter
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clearSession() {
        user = null;
    }
}
