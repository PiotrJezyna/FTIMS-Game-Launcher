package fgl.admin;

import fgl.product.Game;
import fgl.product.GameManager;
import fgl.userPanel.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportedGameBox extends HBox {
  ReportedGameBox( Game game, ModerationPanel mp ) {
    super();
    Label label = new Label();
    label.setText( game.getTitle() );
    label.setMaxWidth( Double.MAX_VALUE );
    HBox.setHgrow( label, Priority.ALWAYS );
    List<GameReport> finalReports = makeReportsListFor( game );
    Button discard = new Button( "Discard" );
    discard.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        mp.discardReport( game, finalReports );
      }
    } );

    Button delete = new Button( "Delete" );
    delete.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        mp.deleteGame( game, finalReports );
      }
    } );
    Button showWhy = makeShowWhyButton( finalReports, mp );
    Button show = new Button( "Show game card" );
    show.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(
                  getClass().getResource( "/ProductCard.fxml" )
          );
          Parent root = fxmlLoader.load();
          Stage stage = new Stage();
          stage.setTitle( "Reported Game Product Card" );
          stage.setScene( new Scene( root ) );
          stage.show();
          GameManager gm = fxmlLoader.getController();
          gm.ShowProductCard( game );
        } catch ( IOException e ) {
          e.printStackTrace();
        } catch ( SQLException e ) {
          e.printStackTrace();
        }
      }
    } );
    this.getChildren().addAll( label, discard, delete, showWhy, show );
  }

  private List<GameReport> makeReportsListFor( Game game ) {
    List<GameReport> reports = new ArrayList<>();
    try {
      reports = ModerationPanel.reportDAO.getAllFor( game.getId() );
    } catch ( SQLException e ) {
      e.printStackTrace();
    }
    return reports;
  }

  private Button makeShowWhyButton( List<GameReport> finalReports, ModerationPanel mp ) {
    Button showWhy = new Button( "Powody zgłoszeń" );
    showWhy.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "Powody zgłoszeń" );
        alert.setHeaderText( null );
        StringBuilder sb = new StringBuilder();
        for ( GameReport report : finalReports ) {
          for ( User u : mp.users ) {
            if ( u.getId().equals( report.getUserID() ) ) {
              sb.append( u.getUsername() );
              sb.append( " pisze:\n" );
            }
          }
          sb.append( report.getExplanation() );
          sb.append( "\n" );
        }
        alert.setContentText( sb.toString() );
        alert.showAndWait();
      }
    } );
    return showWhy;
  }

}
