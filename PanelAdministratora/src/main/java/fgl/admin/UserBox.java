package fgl.admin;

import fgl.communication.MailHandler;
import fgl.userPanel.User;
import fgl.userPanel.UserType;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class UserBox extends HBox {
  private Button button;

  UserBox( User user, boolean isAdminPage ) {
    super();

    Label label = new Label();
    label.setText( user.getUsername() );
    label.setMaxWidth( Double.MAX_VALUE );
    HBox.setHgrow( label, Priority.ALWAYS );

    this.getChildren().add( label );

    if ( !user.getType().equals( UserType.ADMINISTRATOR ) &&
            !user.getType().equals( UserType.MODERATOR ) ) {
      makeBlockUnblockButton( user );
    }

    if ( isAdminPage ) {
      ChoiceBox<UserType> choiceBox = new ChoiceBox<>();
      choiceBox.getItems().addAll(
              UserType.USER, UserType.MODERATOR, UserType.ADMINISTRATOR );
      choiceBox.setValue( user.getType() );
      choiceBox.getSelectionModel().selectedItemProperty().addListener(
              ( ObservableValue<? extends UserType> observable,
                UserType oldValue, UserType newValue ) -> {
                AdministrationPanel.changePermissions( user, newValue );
                if ( newValue.equals( UserType.ADMINISTRATOR ) ||
                        newValue.equals( UserType.MODERATOR ) ) {
                  this.getChildren().remove( 1 );
                } else {
                  makeBlockUnblockButton( user );
                }
              } );
      this.getChildren().add( choiceBox );
    }
  }

  private void makeBlockUnblockButton( User user ) {
    button = new Button();
    if ( user.isBlocked() ) {
      button = new Button( "Odblokuj użytkownika" );
    } else {
      button = new Button( "Zablokuj użytkownika" );
    }
    button.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        if ( user.isBlocked() ) {
          ModerationPanel.unblockUser( user );
          button.setText( "Zablokuj użytkownika" );
          if ( MailHandler.sendMail( user.getUsername(), user.getEmail(), "unblock" ) ) {
            Alert alert =  new Alert( Alert.AlertType.WARNING );
            alert.setTitle( "Nie udało się wysłać maila" );
            alert.setContentText( "Do użytkownika o nicku: " + user.getUsername() +
                    " i adresie email: " + user.getEmail() +
                    " nie udało się wysłać maila z informacją o odblokowaniu konta." );
            alert.showAndWait();
          }
        } else {
          ModerationPanel.blockUser( user );
          button.setText( "Odblokuj użytkownika" );
          if ( MailHandler.sendMail( user.getUsername(), user.getEmail(), "block" ) ) {
            Alert alert =  new Alert( Alert.AlertType.WARNING );
            alert.setTitle( "Nie udało się wysłać maila" );
            alert.setContentText( "Do użytkownika o nicku: " + user.getUsername() +
                    " oraz adresie email: " + user.getEmail() +
                    " nie udało się wysłać maila z informacją o zablokowaniu konta." );
            alert.showAndWait();
          }
        }
      }
    } );
    this.getChildren().add( 1, button );
  }

}

