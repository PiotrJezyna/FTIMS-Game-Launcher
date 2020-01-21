package fgl.catalog;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserContener  implements Cloneable {
    private String searchPhrase = new String();
    private UserDAO userDao = new UserDAO();

    private List<User> displayedUsers = new ArrayList<User>();

    public List<User> getDisplayedUsers() throws SQLException {
        updateDisplayedUsers();
        return displayedUsers;
    }

    public int getRecordCount() {
        return displayedUsers.size();
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public void main(String[] args ) {

    }

    public void updateDisplayedUsers() throws SQLException {
        List<User> users = userDao.getAll();
        displayedUsers.clear();

        for (User user: users) {
            boolean searchNameFlag = false;
            boolean searchUsernameFlag = false;
            boolean searchSurnameFlag = false;
            boolean searchEmailFlag = false;
            boolean emptySearch = false;

            if (!searchPhrase.isEmpty())
            {
                searchNameFlag = (user.getName() != null)           && (user.getName().equals(searchPhrase));
                searchUsernameFlag = (user.getUsername() != null)   && (user.getUsername().equals(searchPhrase));
                searchSurnameFlag = (user.getSurname() != null)     && (user.getSurname().equals(searchPhrase));
                searchEmailFlag = (user.getEmail() != null)         && (user.getEmail().equals(searchPhrase));
            }
            else
                emptySearch = true;

            if ( searchUsernameFlag || searchNameFlag || searchSurnameFlag || searchEmailFlag || emptySearch )
                displayedUsers.add(user);

        }
    }


}
