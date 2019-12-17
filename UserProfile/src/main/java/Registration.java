import java.sql.SQLException;
import java.util.List;

public class Registration {
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Long id;

    public boolean createUser(String username, String name, String surname, String email) throws SQLException {
        UserDAO dao = new UserDAO();
        List<User> allUsers = dao.getAll();
                User user = new User(name, surname, username, email);
                dao.insert(user);
                boolean checkFlag = true;
                return checkFlag;
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



}
