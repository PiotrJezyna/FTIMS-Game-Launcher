package fgl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.EnumType;

import java.util.Set;

@Entity
@Table( name = "Users" )
public class User {
    @Id
    @GeneratedValue
    @Column( name = "ID" )
    private int id;

    @Column( name = "Name" )
    private String name;

    @Column( name = "Surname" )
    private String surname;

    @Column( name = "Username" )
    private String username;

    @Column( name = "Email" )
    private String email;

    @Column( name = "Password" )
    private String password;

    @Enumerated( EnumType.STRING )
    @Column( name = "Type" )
    private UserType type;

    @Column( name = "IsBlocked" )
    private boolean blocked;

    @OneToMany( fetch = FetchType.EAGER, mappedBy = "user"  )
    private Set<Game> games;

    @ManyToMany( fetch = FetchType.EAGER, cascade = { CascadeType.ALL } )
    @JoinTable(
            name = "Users_Games",
            joinColumns = { @JoinColumn( name = "UserID" ) },
            inverseJoinColumns = { @JoinColumn( name = "GameID" ) }
    )
    private Set<Game> installedGames;

    public User() {

    }

    public User( String name, String surname, String email,
                 String password, boolean active ) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.blocked = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname( String surname ) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(  String username ) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public UserType getType() {
        return type;
    }

    public void setType( UserType type ) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public boolean getBlocked() {
        return blocked;
    }

    public void setBlocked ( boolean blocked ) {
        this.blocked = blocked;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames( Set<Game> games  ) {
        this.games = games;
    }

    public Set<Game> getInstalledGames() {
        return installedGames;
    }

    public void setInstalledGames( Set<Game> installedGames ) {
        this.installedGames = installedGames;
    }
}
