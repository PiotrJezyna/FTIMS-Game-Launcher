package fgl.userPanel;

public class UserSession {
    private static UserSession userSession;

    private User currentUser;
    private String confirmationCode;

    private UserSession() {
    }

    public static UserSession getUserSession() {
        if (userSession == null)
            userSession = new UserSession();

        return userSession;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    void setCurrentUser( User user ) {
        this.currentUser = user;
    }

    public String getConfirmationCode() {
        return this.confirmationCode;
    }

    void setConfirmationCode( String code ) {
        this.confirmationCode = code;
    }
}
