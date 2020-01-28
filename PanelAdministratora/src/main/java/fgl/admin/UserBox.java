package fgl.admin;

import fgl.communication.MailHandler;
import fgl.userPanel.User;
import fgl.userPanel.UserType;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Optional;

public class UserBox extends HBox {
  private Button button;
  private ChoiceBox<UserType> choiceBox;
  private UserType actualType;

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
      makeUserTypeChoiceBox( user );
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
          if ( ModerationPanel.unblockUser( user ) ) {
            button.setText( "Zablokuj użytkownika" );
            choiceBox.setDisable( false );
            if ( !MailHandler.sendMail( user.getUsername(), user.getEmail(), "unblock" ) ) {
              Alert alert =  new Alert( Alert.AlertType.WARNING );
              alert.setTitle( "Nie udało się wysłać maila" );
              alert.setContentText( "Do użytkownika o nicku: " + user.getUsername() +
                      " i adresie email: " + user.getEmail() +
                      " nie udało się wysłać maila z informacją o odblokowaniu konta." );
              alert.showAndWait();
            }
          }
        } else {
          if ( ModerationPanel.blockUser( user ) ) {
            button.setText( "Odblokuj użytkownika" );
            choiceBox.setDisable( true );
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
      }
    } );
    this.getChildren().add( 1, button );
  }

  private void makeUserTypeChoiceBox( User user ) {
    choiceBox = new ChoiceBox<>();
    choiceBox.getItems().addAll(
            UserType.USER, UserType.MODERATOR, UserType.ADMINISTRATOR );
    actualType = user.getType();
    choiceBox.setValue( actualType );
    choiceBox.getSelectionModel().selectedItemProperty().addListener(
            ( ObservableValue<? extends UserType> observable,
              UserType oldValue, UserType newValue ) -> {
              if ( newValue.equals( actualType ) ) {
                return;
              }
              Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
              alert.setTitle( "Potwierdzenie" );
              alert.setHeaderText( null );
              alert.setContentText(
                      "Jesteś pewien że chcesz zmienić uprawnienia użytkownikowi " +
                      user.getUsername() + " ?" );
              Optional<ButtonType> result = alert.showAndWait();
              if ( result.get() == ButtonType.OK ) {
                actualType = newValue;
                if ( AdministrationPanel.changePermissions( user, actualType ) ) {
                  if ( newValue.equals( UserType.ADMINISTRATOR ) ||
                          newValue.equals( UserType.MODERATOR ) ) {
                    this.getChildren().remove( button );
                  } else {
                    makeBlockUnblockButton( user );
                  }
                }
              } else {
                choiceBox.setValue( actualType );
              }
            } );
    choiceBox.setDisable( user.isBlocked() );
    this.getChildren().add( choiceBox );
  }

}

