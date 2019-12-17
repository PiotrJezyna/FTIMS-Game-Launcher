public class UserSession {
    private User currentUser;

    public UserSession(User currentUser) {
        this.currentUser = currentUser;
    }

    private void startUserSession(String userName) {

    }

    private void closeUserSession() {

    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
