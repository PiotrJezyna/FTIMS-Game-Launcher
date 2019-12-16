public interface LibraryObserver {
    void onGameStart();
    void onGameClosed(int userID) throws ClassNotFoundException;
}
