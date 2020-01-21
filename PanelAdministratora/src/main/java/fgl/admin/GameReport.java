package fgl.admin;

public class GameReport {
  private long userID;
  private long gameID;
  private String explanation;
  private boolean status;

  public GameReport( long userID, long gameID, String explanation, boolean status ) {
    this.userID = userID;
    this.gameID = gameID;
    this.explanation = explanation;
    this.status = status;
  }

  public long getUserID() {
    return userID;
  }

  public void setUserID( long userID ) {
    this.userID = userID;
  }

  public long getGameID() {
    return gameID;
  }

  public void setGameID( long gameID ) {
    this.gameID = gameID;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation( String explanation ) {
    this.explanation = explanation;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus( boolean status ) {
    this.status = status;
  }
}
