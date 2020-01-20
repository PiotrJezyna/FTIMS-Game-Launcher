import fgl.database.AbstractDao;
import fgl.product.Game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalGamesDAO extends AbstractDao<Game> {

    private File path;
    private File defaultPath;
    private FileWriter fileWriter = null;

    public LocalGamesDAO(){
        this.path = new File("C:\\FtimsGameLauncher");
        defaultPath = path;
        if(!defaultPath.exists()) defaultPath.mkdir();
        try{
            fileWriter = new FileWriter(defaultPath.getAbsolutePath()+"\\file.txt");
            fileWriter.write(defaultPath.getAbsolutePath());
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void changePath(File path){
        this.path = path;
    }

    public File getPath(){
        return path;
    }

    public File getDefaultPath() {
        return defaultPath;
    }

    @Override
    protected Game get(Long id) throws SQLException {
        return null;
    }

    @Override
    protected List<Game> getAll() throws SQLException {

        List<Game> localGames = new ArrayList<>();

        String[] pathnames;
        pathnames = this.path.list();

        for (String pathname : pathnames) {

            File tmpDir = new File(this.path.getAbsolutePath()+ "\\" + pathname + "\\" + pathname + ".exe");
            if (tmpDir.exists())
            {
                connectSQL();

                String query = "SELECT ID, UserID, Tags, UserCount, IsReported  FROM Games WHERE Title = '" + pathname + "'";

                stmt = conn.createStatement();
                rs = stmt.executeQuery( query );
                if (rs.next())
                {
                    System.out.println(pathname);

                    Long gameId = rs.getLong("ID");
                    Long userId = rs.getLong("UserID");
                    String tags = rs.getString("Tags");
                    Integer userCount = rs.getInt("UserCount");
                    boolean isReported = rs.getBoolean("IsReported");

                    localGames.add(new Game(gameId, userId, pathname, tags, null, null, userCount, isReported));
                }

                disconnectSQL();
            }
        }

        return localGames;
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
