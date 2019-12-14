package fgl.admin;

import fgl.userPanel.User;
import fgl.userPanel.UserType;

import java.sql.SQLException;

public class AdministrationPanel extends ModerationPanel {

  private static final String TITLE = "Admin Panel";
  private static final String PATH = "/AdminPanel.fxml";

  public AdministrationPanel() {
    super();
  }

  @Override
  public String getTitle() {
    return TITLE;
  }

  @Override
  public String getPath() {
    return PATH;
  }

  public boolean changePermissions( User user, UserType userType ) {
    try {
      user.setType( userType );
      super.getUserDAO().update( user );
    } catch ( SQLException e ) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
