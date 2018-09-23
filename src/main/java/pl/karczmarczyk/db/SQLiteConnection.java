package pl.karczmarczyk.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.SQLite;
import org.bukkit.Location;
import pl.karczmarczyk.ownplace.OwnPlace;
import pl.karczmarczyk.utils.PlotUtils;

/**
 *
 * @author mateusz
 */
public class SQLiteConnection {
    
    private SQLite sqlite;
    
    private OwnPlace plugin;
    private String prefix;
    private String directory;
    private String tablename;
    
    final boolean debugMode = true;
    
    public SQLiteConnection(OwnPlace plugin, String prefix, String directory, String tablename) {
        this.plugin = plugin;
        this.prefix = prefix;
        this.directory = directory;
        this. tablename = tablename;
        sqlConnection();
        sqlTableCheck();
    }
    
    private void sqlConnection() {
        debugLog("getDataFolder"+this.directory);
        sqlite = new SQLite(getLogger(),
            this.prefix,
            this.directory,
            this.tablename
        );
        try {
            sqlite.open();
        } catch (Exception e) {
            getLogger().info(e.getMessage());
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }

    private void sqlTableCheck() {
        if (!sqlite.checkTable("own_doors")) {
            String sql = "CREATE TABLE own_doors (id INTEGER PRIMARY KEY AUTOINCREMENT, owner VARCHAR(50), x int, y int, z int);";
            query(sql);
            debugLog("own_doors has been created");
        }
        if (!sqlite.checkTable("own_door_to_player")) {
            String sql = "CREATE TABLE own_door_to_player (id INTEGER PRIMARY KEY AUTOINCREMENT, door_id int, player VARCHAR(50));";
            query(sql);
            debugLog("own_door_to_player has been created");
        }
        if (!sqlite.checkTable("own_plots")) {
            String sql = "CREATE TABLE own_plots (id INTEGER PRIMARY KEY AUTOINCREMENT, x1 int, z1 int, x2 int, z2 int, area int, owner VARCHAR(50), description text);";
            query(sql);
            debugLog("own_plots has been created");
        }
    }
    
    private Logger getLogger() {
        return plugin.getLogger();
    }
    
    private void debugLog(String msg) {
        if(debugMode) {
            getLogger().info(msg);
        }
    }
    
    public void close() {
        sqlite.close();
    }
    
    public ResultSet query (String sql) {
        ResultSet rs = null;
        try {
            debugLog("SQL:"+sql);
            rs = sqlite.query(sql);
        } catch (SQLException ex) {
            debugLog(ex.getMessage());
            ex.printStackTrace();
        }
        return rs;
    }
    
    public void addObj(Location location, String userUuid) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        
        this.deleteObj(location);
        String sql = "INSERT INTO own_doors(owner, x, y, z) VALUES('"+userUuid+"', "+x+","+y+","+z+");";
        query(sql);
    }
    
    public ResultSet getObj(Location location) {
        return getObj(location, null);
    }
    
    public ResultSet getObj(Location location, String userUuid) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        ResultSet result = null;
        if (null==userUuid) {
            String sql = "SELECT * FROM own_doors WHERE (x="+x+" AND y="+y+" AND z="+z+") OR (x="+x+" AND y="+(y-1)+" AND z="+z+");";
            result = query(sql);
        } else {
            String sql = "SELECT * FROM own_doors WHERE ((x="+x+" AND y="+y+" AND z="+z+") OR (x="+x+" AND y="+(y-1)+" AND z="+z+")) AND owner='"+userUuid+"';";
            result = query(sql);
        }
        return result;
    }
    
    public ResultSet getObj(int objId, String userUuid) {
        ResultSet result = null;
        String sql = "SELECT * FROM own_doors WHERE id="+objId+" AND owner='"+userUuid+"';";
        result = query(sql);
        return result;
    }
    
    public void deleteObj(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        ResultSet rs = getObj(location);
        String sql = "DELETE FROM own_doors WHERE x="+x+" AND y="+y+" AND z="+z+";";
        query(sql);
        try {
            while(rs.next()) {
                unshareObj(rs.getInt("id"));
            }
        } catch (SQLException ex) {
            
        }
    }
    
    public void deleteObj(int objId, String userUuid) {
        String sql = "DELETE FROM own_doors WHERE id="+objId+" AND owner='"+userUuid+"';";
        query(sql);
        unshareObj(objId);
    }
    
    public Integer getLastId() {
        String sql = "SELECT last_insert_rowid()";
        ResultSet rs = query(sql);
        try {
            if (null!=rs && rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ResultSet getObjList(String uuid) {
        String sql = "SELECT * from own_doors where owner = '"+uuid+"'";
        return query(sql);
    }
    
    public ResultSet getSharedFor (int doorId, String userUuid) {
        String sql = "SELECT * FROM own_door_to_player WHERE door_id="+doorId+" AND player='"+userUuid+"';";
        return query(sql);
    }
    
    public void shareObj(int doorId, String userUuid) {
        String sql = "INSERT INTO own_door_to_player (door_id, player) VALUES("+doorId+",'"+userUuid+"');";
        query(sql);
    }
    
    public void unshareObj(int doorId, String userUuid) {
        String sql = "DELETE FROM own_door_to_player WHERE door_id="+doorId+" AND player='"+userUuid+"';";
        query(sql);
    }
    
    public void unshareObj(int doorId) {
        String sql = "DELETE FROM own_door_to_player WHERE door_id="+doorId+";";
        query(sql);
    }
    
    public void addPlot(Location location1, Location location2, String userUuid, int isActive, String description) {
        int x1 = new Double(location1.getX()).intValue();
        int z1 = new Double(location1.getZ()).intValue();
        int x2 = new Double(location2.getX()).intValue();
        int z2 = new Double(location2.getZ()).intValue();
        addPlot(x1, z1, x2, z2, userUuid, isActive, description);
    }
    
    public void addPlot (int x1, int z1, int x2, int z2, String userUuid, int isActive, String description) {
        int area = PlotUtils.calcField(x1, z1, x2, z2);
        String sql = "INSERT INTO own_plots (x1, z1, x2, z2, area, owner, is_active, description) VALUES("+x1+", "+z1+", "+x2+", "+z2+", "+area+", '"+userUuid+"', "+isActive+", '"+description+"');";
        query(sql);
    }
    
    public void removePlot (int plotId, String userUuid) {
        String sql = "DELETE FROM own_plots WHERE id="+plotId+" AND owner='"+userUuid+"';";
        query(sql);
    }
    
    public ResultSet getPlots () {
        String sql = "SELECT * FROM own_plots;";
        return query(sql);
    }
}
