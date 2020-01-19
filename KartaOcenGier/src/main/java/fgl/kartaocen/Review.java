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
    private int rating; // TODO: Add new class for rating

    // ========================================================= Behaviour == //
    public Review() {
    }

    public Review(Long id, Game game, User user, int rating) {
        this.id = id;
        this.game = game;
        this.user = user;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

// ////////////////////////////////////////////////////////////////////////// //

