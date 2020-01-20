public interface LibraryObserver {
    void onGameStart(Long userID, Long gameID) throws ClassNotFoundException;
    void onGameClosed(Long userID, Long gameID) throws ClassNotFoundException;
}
