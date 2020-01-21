package fgl.userPanel;

import fgl.communication.MailHandler;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class Registration {
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private static UserSession userSession;
    private Long id;

    public boolean createUser(String username, String name, String surname, String email, String password) throws SQLException, NoSuchAlgorithmException {
        UserDAO dao = new UserDAO();

        if (checkForMailAndUsername(username, email)){
            String confirmationString = generateRandomString();
            User user = new User(name, surname, username, email, password, confirmationString);
            UserSession.getUserSession().setConfirmationCode( confirmationString );
            //MailHandler.sendMailWithCode(user.getUsername(), user.getEmail(), "registration", confirmationString);
            dao.insert(user);

            return true;
        } else {
            return false;
        }

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    private boolean checkForMailAndUsername(String uUserName, String uEmail) throws SQLException {
        UserDAO dao = new UserDAO();
        List<User> allUsers = dao.getAll();
        boolean flag = true;

        for (int i = 0; i < allUsers.size(); i++) {
            if (uUserName.equals(allUsers.get(i).getUsername())) {
                flag = false;
            } else if (uEmail.equals(allUsers.get(i).getEmail())) {
                flag = false;
            }
        }
        return flag;
    }

    public String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(generatedString);
        return generatedString;
    }


}
