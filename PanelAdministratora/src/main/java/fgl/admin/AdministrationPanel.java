package fgl.admin;

import fgl.userPanel.User;
import fgl.userPanel.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministrationPanel extends ModerationPanel {

  private static final String TITLE = "Admin Panel";
  private static final String PATH = "/AdminPanel.fxml";

  public AdministrationPanel() {
  }

  @Override
  public boolean loadAllUsersFromDB() {
    try {
      users = userDAO.getAll();
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }

    List<UserBox> list = new ArrayList<>();

    for ( User user: users ) {
      list.add( new UserBox( user, true ) );
    }

    ObservableList<UserBox> myObservableList = FXCollections.observableList( list );
    usersListView.setItems( myObservableList );

    return true;
  }

  @Override
  public String getTitle() {
    return TITLE;
  }

  @Override
  public String getPath() {
    return PATH;
  }

  public static boolean changePermissions( User user, UserType userType ) {
    try {
      user.setType( userType );
      userDAO.update( user );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
