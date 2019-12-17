package fgl.userPanel;

public class UserSession {
    private User currentUser;

    UserSession() {

    }

    UserSession(User currentUser) {
        this.currentUser = currentUser;
    }


    void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
