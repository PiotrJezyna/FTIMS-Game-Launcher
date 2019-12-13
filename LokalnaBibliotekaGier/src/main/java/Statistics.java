import javafx.scene.paint.Stop;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Date;

public class Statistics implements LibraryObserver{
    private int statisticsID;
    private double gameTime;
    private Date installationDate;
    private Date recentlyPlayed;
    private StopWatch stopWatch = new StopWatch();

    public double getGameTime() {
        return gameTime;
    }

    public void setGameTime(double gameTime) {
        this.gameTime += gameTime;
    }

    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    public Date getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public void setRecentlyPlayed(Date recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public void showAllStatistics(){
        String allStatistics = gameTime + installationDate.toString() + recentlyPlayed.toString();
    }

    public void onGameStart(){
        stopWatch.start();
    }

    public void onGameClosed(){
        stopWatch.stop();
        setGameTime(stopWatch.getTime());
        System.out.println(getGameTime());
    }
}
