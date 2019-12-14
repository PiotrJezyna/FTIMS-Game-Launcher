package fgl.admin;

import fgl.userPanel.User;
import fgl.userPanel.UserType;

import java.sql.SQLException;

public class AdministrationPanel extends ModerationPanel {

  private static final String title = "Admin Panel";
  private static final String path = "/AdminPanel.fxml";

  public AdministrationPanel() {
    super();
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getPath() {
    return path;
  }

  public boolean changePermissions(User user, UserType userType ) {
    try {
      user.setType( userType );
      super.getUserDAO().update( user );
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
