import java.sql.SQLException;
import java.util.List;

public class Login {
    private String email;
    private String password;
    private UserDAO dao = new UserDAO();
    private static UserSession userSession;

    public boolean validate(String email, String surname) throws SQLException {
        List<User> allUsers = dao.getAll();

        boolean checkFlag = false;
        for (int i = 0; i < allUsers.size(); i++) {
            if (email.equals(allUsers.get(i).getEmail()) && surname.equals(allUsers.get(i).getSurname())) {
                UserSession userSessionBegan = new UserSession(allUsers.get(i));
                userSession = userSessionBegan;
                System.out.println("Successfully logged in");
                checkFlag = true;
            }
        }
        return checkFlag;
    }

    public UserSession getUserSession() {
        return this.userSession;
    }



}
