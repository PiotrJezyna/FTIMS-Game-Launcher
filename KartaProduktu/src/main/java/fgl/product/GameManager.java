package fgl.product;
package fgl.userPanel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;


public class GameManager {

    GameDAO dao = new GameDAO();
    List <Game> games = dao.getAll();

    public Game GetProductCard(String title)
    {
        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle() == title)
            {
                return games.get(i);
            }
        }
        System.out.println("Game " + title + "not found");
        return null;
    }

    public void ShowProductCard(String title)
    {
        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle() == title)
            {
                System.out.println("Game: " + games.get(i).getTitle());
                System.out.println("Author: " + games.get(i).getUserId());
                System.out.println("Tags: " + games.get(i).getTags());
                System.out.println("Path: " + games.get(i).getPath());
                System.out.println("Genre: " + games.get(i).getGenre());
                System.out.println("Description: " + games.get(i).getDescription());
            }
        }
    }

    public void RemoveProductCard(String title, User user)
    {
        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle() == title)
            {
                games.remove(i);
                dao.delete(games.get(i));
                System.out.println("Game " + title + " successfully deleted.");
            }
        }
        System.out.println("Game " + title + "not found. Game wasn't deleted");
    }

    public void CreateProductCard(Long userId, String title, String tags, String path, String genre, String description)
    {
        Game game = new Game(userId, title, tags, path, genre, description);
        games.add(game);
        dao.insert(game);
    }

    // this should be written better, some overrides
    public void EditProductCard(Long userId, String title, String tags, String path, String genre, String description)
    {
        for(int i = 0; i < games.size(); i++)
        {
            if(games.get(i).getTitle() == title)
            {
                //games.get(i).setUserCount(userId); ???

                dao.delete(games.get(i));
                games.get(i).setTitle(title);
                games.get(i).setTags(tags);
                games.get(i).setPath(path);
                games.get(i).setGenre(genre);
                games.get(i).setDescription(description);
                dao.update(games.get(i));
                System.out.println("Game " + title + "successfully edited and saved.");
            }
        }
        System.out.println("Game " + title + "not found");
    }

    // ---according to the class scheme from the presentation--
    /*
    public void CreateProductCard(String title, User author, String linkToDownload, String md5, String genre, List<String> tags,
                      String description, List<Image> screenshots, Image icon, int downloads)
    {
        Game game = new Game(title, author, linkToDownload, md5, genre, tags, description, screenshots, icon, downloads);
    }

    public void EditProductCard(String title, User author, String linkToDownload, String md5, String genre, List<String> tags,
                                  String description, List<Image> screenshots, Image icon, int downloads)
    {

    } */
}
