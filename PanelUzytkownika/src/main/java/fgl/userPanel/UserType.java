package fgl.userPanel;

public enum UserType {
  ADMINISTRATOR( "ADMINISTRATOR" ),
  MODERATOR( "MODERATOR" ),
  USER( "USER" );

  private String name;

  UserType( String name ) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
