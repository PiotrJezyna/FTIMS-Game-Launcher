package fgl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";

    static final String USER = "5VexXpVWzU";
    static final String PASS = "apQqybLdoW";

    @Override
    public void start(Stage primaryStage) throws IOException {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, Name, Surname FROM Users";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                int id  = rs.getInt("id");
                String first = rs.getString("Name");
                String last = rs.getString("Surname");

                System.out.print("ID: " + id);
                System.out.print(", First: " + first);
                System.out.println(", Last: " + last);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch(SQLException se2) {
            }

            try {
                if(conn!=null)
                    conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }

        Parent root = FXMLLoader.load(getClass().getResource("/Launcher.fxml"));
        primaryStage.setTitle("FTIMS Game Launcher");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
