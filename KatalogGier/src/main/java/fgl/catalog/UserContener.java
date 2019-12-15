package fgl.catalog;
import fgl.userPanel.User;

import java.util.ArrayList;
import java.util.List;

import static fgl.userPanel.UserType.ADMINISTRATOR;

public class UserContener {
    private static String searchPhrase = new String();
    private static List<User> allUsers = new ArrayList<User>();
    private static List<User> displayedUsers= new ArrayList<User>();

 public void updateDisplayedUsers()
 {
     for (User user: allUsers) {


         boolean searchnameFlag =false;
         boolean searchUsernameFlag =false;
         boolean searchSurnameFlag =false;
         boolean searchEmailFlag =false;

         if (!searchPhrase.isEmpty())
         {
             searchnameFlag = (user.getName().equals(searchPhrase));
             searchUsernameFlag = (user.getUsername().equals(searchPhrase));
             searchSurnameFlag = (user.getSurname().equals(searchPhrase));
             searchEmailFlag = (user.getEmail().equals(searchPhrase));

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
    public static void main( String[] args ) {
        UserContener k1= new UserContener();
        k1.setSearchPhrase("Jakub");
        Long a = new Long(3);
        String s = new String();
        User user1 = new  User( a,
               "Jakub",
                "Grobelkiewicz",
                "Majster",
                "kekw@gmail.com",
                ADMINISTRATOR,
       false);
        User user12 = new  User( a,
                "Jakub",
                "nie",
                "Majsterko",
                "rageww@gmail.com",
                ADMINISTRATOR,
                false);
        User user13 = new  User( a,
                "Szymon",
                "nie",
                "Majsterko",
                "rageww@gmail.com",
                ADMINISTRATOR,
                false);
        k1.allUsers.add(user1);
        k1.allUsers.add(user12);
        k1.allUsers.add(user13);
        k1.updateDisplayedUsers();
        for (User user: displayedUsers) {
           System.out.println(user.getName()+user.getSurname());
       }

    }
}
