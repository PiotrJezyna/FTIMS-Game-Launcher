package fgl.catalog;

import fgl.product.Game;

import java.util.ArrayList;
import java.util.List;

public class GameContener {

    private static List<Game> allGames = new ArrayList<Game>();
    private static List<Game> displayedGames = new ArrayList<Game>();


    private static List<String> tags = new ArrayList<String>();
    private static int category;   // 0 - no category (display all)
    private static int typeOfSort; // 0 - no sort
    private static String searchPhrase = new String();

    public static void main( String[] args ) {
        Long a = new Long(3);
        String s = new String();
        Game game1 = new Game(new Long(3), new String("DMC"), new String("slasher"), s);
        Game game2 = new Game(new Long(3), new String("TWAU"), new String("story"), s);
        Game game3 = new Game(new Long(3), new String("GOW"), new String(), s);

        allGames.add(game1);
        allGames.add(game2);
        allGames.add(game3);

        tags.add(new String("story"));
        tags.add(new String("slasher"));
        updateDisplayedGames();

        for (Game game: displayedGames) {
            System.out.println(game.getTitle());
        }
    }

    public List<Game> getDisplayedGames() {
        return displayedGames;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setTypeOfSort(int typeOfSort) {
        this.typeOfSort = typeOfSort;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public static void updateDisplayedGames()
    {
        for (Game game: allGames) {

            boolean categoryFlag = true;
            boolean searchPhraseFlag = true;
            boolean tagsFlag = true;

            /*if (category != 0)
                categoryFlag = (game.getCategory() == category);*/

            if (!searchPhrase.isEmpty())
                searchPhraseFlag = (game.getTitle().equals(searchPhrase));

            if (!tags.isEmpty())
            {
                tagsFlag = false;
                for (String tag: tags) {
                    if (game.getTags().equals(tag))
                    {
                        tagsFlag = true;
                        break;
                    }
                }
            }

            if (categoryFlag && searchPhraseFlag && tagsFlag)
                displayedGames.add(game);

        }
            

    }

}
