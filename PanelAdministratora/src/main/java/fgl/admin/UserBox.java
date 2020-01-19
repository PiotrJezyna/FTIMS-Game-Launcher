package fgl.admin;

import fgl.communication.MailHandler;
import fgl.userPanel.User;
import fgl.userPanel.UserType;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class UserBox extends HBox {
  User user;
  Label label = new Label();
  Button button = new Button();
  ChoiceBox<UserType> choiceBox = new ChoiceBox<>();

  UserBox( User user, boolean isAdminPage ) {
    super();

    this.user = user;
    label.setText( user.getUsername() );
    label.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(label, Priority.ALWAYS);

    if ( user.isBlocked() ) {
      button = new Button( "Unblock user" );
    } else {
      button = new Button( "Block user" );
    }
    button.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        if ( user.isBlocked() ) {
          ModerationPanel.unblockUser( user );
          button.setText( "Block user" );
          MailHandler.sendMail( user, "unblock" );
        } else {
          ModerationPanel.blockUser( user );
          button.setText( "Unblock user" );
          MailHandler.sendMail( user, "block" );
        }
      }
    });

    if ( isAdminPage ) {
      choiceBox.getItems().addAll( UserType.USER, UserType.MODERATOR, UserType.ADMINISTRATOR );
      choiceBox.setValue( user.getType() );
      choiceBox.getSelectionModel().selectedItemProperty()
               .addListener( (ObservableValue<? extends UserType> observable, UserType oldValue, UserType newValue ) -> {
                System.out.println( newValue );
                user.setType( newValue );
                AdministrationPanel.changePermissions( user, newValue );
               } );
      this.getChildren().addAll( label, button, choiceBox );
    } else {
      this.getChildren().addAll( label, button );
    }
  }
}
