package main.java;

import java.util.Date;

public class Review {
    private String comment;
    private int rating;
    private String authorsReply;
    private Date date;
    private Game game;
    private User user;

    public Review(String comment, int rating, String authorsReply, Date date, Game game, User user) {
        this.comment = comment;
        this.rating = rating;
        this.authorsReply = authorsReply;
        this.date = date;
        this.game = game;
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAuthorsReply() {
        return authorsReply;
    }

    public void setAuthorsReply(String authorsReply) {
        this.authorsReply = authorsReply;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
