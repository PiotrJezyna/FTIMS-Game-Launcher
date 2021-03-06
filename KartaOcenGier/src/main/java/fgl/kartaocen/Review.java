// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// =================================================================== FGL == //
import fgl.product.Game;
import fgl.userPanel.User;

// //////////////////////////////////////////////////////////// Class: Review //
public class Review {

    // ============================================================== Data == //
    private Long id;
    private Game game;
    private User user;
    private int  rating; // TODO: Add new class for rating

    // ========================================================= Behaviour == //
    public Review(Game game, User user, int rating) {
        this.id = 0L;
        this.game = game;
        this.user = user;
        this.rating = rating;
    }

    public Review(Long id, Game game, User user, int rating) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.rating = rating;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

// ////////////////////////////////////////////////////////////////////////// //

