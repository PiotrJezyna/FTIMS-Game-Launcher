import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalLibrary {
    private int libraryID;
    private List<Statistics> gameList;
    //private ProductManager productManager;
    private boolean isPlaying;
    private List<GameDownloader> downloaderList;
    private boolean isDownloading;
    public LocalLibrary(){
        gameList = new ArrayList<Statistics>();
        downloaderList = new ArrayList<GameDownloader>();
    }
    public int getLibraryID() {
        return this.libraryID;
    }
    public void play(){
        Runtime runtime = Runtime.getRuntime();
        Statistics statistics = new Statistics();
        this.gameList.add(statistics);
        try {
            gameList.get(0).onGameStart();
            isPlaying = true;
            Process process = runtime.exec("C:\\GOG Games\\Return of the Obra Dinn\\ObraDinn.exe", null, new File("C:\\GOG Games\\Return of the Obra Dinn\\"));
            process.waitFor();
            gameList.get(0).onGameClosed();
            isPlaying = false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
