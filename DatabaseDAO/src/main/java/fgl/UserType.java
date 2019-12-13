package fgl;

public enum UserType {

    ADMINISTRATOR( "Administrator" ), MODERATOR( "Moderator" ), USER( "User" );

    private String name;

    UserType( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
