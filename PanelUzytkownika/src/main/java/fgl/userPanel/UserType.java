package fgl.userPanel;

public enum UserType {
    ADMINISTRATOR( "ADMINISTRATOR" ),
    MODERATOR( "MODERATOR" ),
    USER( "USER" );

    String name;

    private UserType(String name) {
        this.name = name;
    }
}