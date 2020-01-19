package fgl.userPanel;

import java.sql.SQLException;
import java.util.List;

public class Login {

    private UserDAO dao = new UserDAO();
    public static UserSession userSession;

    public boolean validate(String username, String password) throws SQLException {
        List<User> allUsers = dao.getAll();

        boolean checkFlag = false;
        for (int i = 0; i < allUsers.size(); i++) {
            if ( password.equals(allUsers.get(i).getPassword()) && username.equals(allUsers.get(i).getUsername())) {
                UserSession userSessionBegan = new UserSession(allUsers.get(i) );
                userSession = userSessionBegan;
                System.out.println("Successfully logged in");
                System.out.println(password);
                System.out.println(allUsers.get(i).getUsername());
                checkFlag = true;
            }

        }

        return checkFlag;
    }

    public UserSession getUserSession() {
        return this.userSession;
    }


}
