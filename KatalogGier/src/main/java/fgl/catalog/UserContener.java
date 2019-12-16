package fgl.catalog;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static fgl.userPanel.UserType.ADMINISTRATOR;

public class UserContener  implements Cloneable {
    private static String searchPhrase = new String();
    private static  UserDAO allUsers = new UserDAO();
    private static List<User> displayedUsers= new ArrayList<User>();

    public void updateDisplayedUsers() throws SQLException {
        List<User> users = allUsers.getAll();

        for (User user: users) {


            boolean searchnameFlag =false;
            boolean searchUsernameFlag =false;
            boolean searchSurnameFlag =false;
            boolean searchEmailFlag =false;

            if (!searchPhrase.isEmpty())
            {
                searchnameFlag = (user.getName() != null) && (user.getName().equals(searchPhrase));
                searchUsernameFlag = (user.getUsername() != null) && (user.getUsername().equals(searchPhrase));
                searchSurnameFlag = (user.getSurname() != null) && (user.getSurname().equals(searchPhrase));
                searchEmailFlag = (user.getEmail() != null) && (user.getEmail().equals(searchPhrase));
            }




            if ( searchUsernameFlag||searchnameFlag ||searchSurnameFlag||searchEmailFlag )
                displayedUsers.add(user);

        }
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }
    public List<User> getDisplayedUsers()
    {

        return displayedUsers;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args ) throws SQLException, CloneNotSupportedException {

        UserContener k1= new UserContener();
        k1.setSearchPhrase(new String("Piotr"));


        k1.updateDisplayedUsers();

        for (User user: displayedUsers) {
            System.out.println(user.getName()+user.getSurname());
        }

    }
}
