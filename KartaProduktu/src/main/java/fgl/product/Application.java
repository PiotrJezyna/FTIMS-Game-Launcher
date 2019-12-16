package fgl.product;

import java.sql.SQLException;
import java.util.List;

public class Application {

    public static void main( String[] args ) throws SQLException {

        GameManager gameManager = new GameManager();

        gameManager.ShowProductCard("The Witcher 3");
    }
}
