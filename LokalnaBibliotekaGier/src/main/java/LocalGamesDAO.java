import fgl.database.AbstractDao;
import fgl.product.Game;
import fgl.product.Main;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalGamesDAO extends AbstractDao<Game> {

    private File path;
    private File defaultPath;
    private FileWriter fileWriter = null;
    private BufferedReader bufferedReader = null;
    private String pathsFile = "\\paths.txt";
    private String[] gameNames = new String[100];
    private String[] gamesFilesPath;
    private File[] gamesFiles;

    public LocalGamesDAO(){
        gamesFiles = new File[100];
        gamesFilesPath = new String[100];
        defaultPath = new File("C:\\FtimsGameLauncher");
        if(!defaultPath.exists()) defaultPath.mkdir();
        String pathName = null;
        while(pathName == null){
            try{
                bufferedReader = new BufferedReader(new FileReader(defaultPath.getAbsoluteFile()+ pathsFile));
                pathName = bufferedReader.readLine();
                if(pathName!=null){
                    path = new File(pathName);
                }
            } catch (IOException e) {
                try {
                    fileWriter = new FileWriter(defaultPath.getAbsolutePath() + pathsFile);
                    fileWriter.write(defaultPath.getAbsolutePath());
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void changePath(File path){
        this.path = path;
        try{
            FileWriter fileWriter = new FileWriter(defaultPath.getAbsolutePath() + pathsFile, true);
            bufferedReader = new BufferedReader(new FileReader(defaultPath.getAbsoluteFile() + pathsFile));
            if(bufferedReader.readLine() != null)
            {
                fileWriter.write("\n");
            }
            fileWriter.write(this.path.getAbsolutePath());
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public File getPath(){
        return path;
    }

    public File getDefaultPath() {
        return defaultPath;
    }

    public String getGamesFilesPath(int index) { return gamesFilesPath[index]; }

    public File getGamesFiles(int index) {
        return gamesFiles[index];
    }

    @Override
    protected Game get(Long id) throws SQLException {
        return null;
    }

    @Override
    protected List<Game> getAll() throws SQLException {

        List<Game> localGames = new ArrayList<>();
        List<File> games = findGame();

        for(int i = 0; i <= games.size(); i++)
        {
            connectSQL();
            String query = "SELECT ID, UserID, Version, Tags, UserCount, IsReported, IsDeleted FROM Games WHERE Title = '" + gameNames[i] + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                Long gameId = rs.getLong("ID");
                Long userId = rs.getLong("UserID");
                Integer version = rs.getInt("Version");
                String tags = rs.getString("Tags");
                Integer userCount = rs.getInt("UserCount");
                boolean isReported = rs.getBoolean("IsReported");
                boolean isDeleted = rs.getBoolean("IsDeleted");
                localGames.add(new Game(gameId, userId, gameNames[i], version, tags, null, null, userCount, isReported, isDeleted));
            }
            disconnectSQL();
        }
        return localGames;
    }

    protected List<File> findGame()
    {
        List<File> results = new ArrayList<>();
        String[] absolutePath = new String[100];
        int lineAmount = 0;

        try {
            bufferedReader = new BufferedReader(new FileReader(defaultPath.getAbsoluteFile() + pathsFile));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                absolutePath[lineAmount] = line;
                lineAmount++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < lineAmount; i++)
        {
            String[] pathnames = new File(absolutePath[i]).list();
            for (String pathname : pathnames) {
                File tmpDir = new File(absolutePath[i] + "\\" + pathname + "\\" + pathname + ".exe");
                if(tmpDir.exists())
                {
                    gameNames[i] = pathname;
                    results.add(tmpDir);
                }
            }
        }

        return results;
    }

    @Override
    protected void insert(Game game) throws SQLException {

    }

    @Override
    protected void update(Game game) throws SQLException {

    }

    @Override
    protected void delete(Game game) throws SQLException {

    }
}
