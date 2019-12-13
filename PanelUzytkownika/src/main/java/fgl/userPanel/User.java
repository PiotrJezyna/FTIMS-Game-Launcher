package fgl.userPanel;

public class User {

    enum UserType { User, Moderator, Administrator };

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private UserType type;
    private boolean isBlocked;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserType getType() {
        return type;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setSurname( String surname ) {
        this.surname = surname;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setType( UserType type ) {
        this.type = type;
    }

    public void setBlocked( boolean blocked ) {
        isBlocked = blocked;
    }
}
