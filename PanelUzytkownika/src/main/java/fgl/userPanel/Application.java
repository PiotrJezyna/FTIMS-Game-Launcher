package fgl.userPanel;

import java.sql.SQLException;
import java.util.List;

public class Application {

    public static void main( String[] args ) throws SQLException {

        UserDAO dao = new UserDAO();

        /// Tworzymy sobie obiekt typu User i dodajemy go do bazy danych
        User user = new User("User", "User@user.com");
        dao.insert(user);

        /// Pobieramy wszystkich userow z bazy danych
        List<User> users = dao.getAll();

        user = users.get(users.size() - 1);

        /// Po pobraniu usera z bazy danych do obiektu edytujemy go i wysylamy zmiany do bazy danych
        user.setName("User");
        user.setSurname("Userski");
        dao.update(user);

        /// Usuwamy usera z bazy danych
        dao.delete(user);
    }
}
