package fgl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import java.util.Set;

@Entity
@Table( name = "Games" )
public class Game {
    @Id
    @GeneratedValue
    @Column( name = "ID" )
    private int id;

    @ManyToOne
    @JoinColumn( name = "UserID", nullable = false )
    private User user;

    @Column( name = "Title" )
    private String title;

    @Column( name = "Tags" )
    private String tags;

    @Column( name = "Path" )
    private String path;

    @Column( name = "UserCount" )
    private int userCount;

    @ManyToMany( fetch = FetchType.EAGER, cascade = { CascadeType.ALL } )
    @JoinTable(
            name = "Games_Genres",
            joinColumns = { @JoinColumn( name = "GameID" ) },
            inverseJoinColumns = { @JoinColumn( name = "GenreID" ) }
    )
    private Set<Genre> genres;

    public Game() {

    }

    public Game( User user, String title, String tags, String path, int userCount ) {
        this.user = user;
        this.title = title;
        this.tags = tags;
        this.path = path;
        this.userCount = userCount;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags( String tags ) {
        this.tags = tags;
    }

    public String getPath() {
        return path;
    }

    public void setPath( String path  ) {
        this.path = path;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount( int userCount ) {
        this.userCount = userCount;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres( Set<Genre> genres ) {
        this.genres = genres;
    }
}
