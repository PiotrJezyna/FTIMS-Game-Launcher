public interface LibraryObserver {
    void onGameStart();
    void onGameClosed(int userID, int gameID) throws ClassNotFoundException;
}
