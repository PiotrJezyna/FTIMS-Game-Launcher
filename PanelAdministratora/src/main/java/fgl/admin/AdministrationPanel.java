package fgl.admin;

import fgl.communication.MailHandler;
import fgl.userPanel.User;
import fgl.userPanel.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministrationPanel extends ModerationPanel {

  private static final String TITLE = "Admin Panel";
  private static final String PATH = "/AdminPanel.fxml";

  public AdministrationPanel() {
  }

  @Override
  protected void makeUsersListView() {
    List<UserBox> list = new ArrayList<>();

    for ( User user : users ) {
      list.add( new UserBox( user, true ) );
    }

    ObservableList<UserBox> myObservableList = FXCollections.observableList( list );
    usersListView.setItems( myObservableList );
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

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Potwierdzenie");
    alert.setHeaderText(null);
    alert.setContentText("Jesteś pewien że chcesz zmienić uprawnienia użytkownikowi " +
            user.getUsername() + " ?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){

      try {
        user.setType( userType );
        userDAO.update( user );
      } catch ( SQLException e ) {
        e.printStackTrace();
        return false;
      }

      MailHandler.sendMailWithNewUserType(user.getUsername(), user.getEmail(), userType.toString());

      return true;
    } else {
      return false;
    }
  }

}
