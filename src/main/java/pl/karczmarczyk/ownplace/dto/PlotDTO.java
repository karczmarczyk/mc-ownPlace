package pl.karczmarczyk.ownplace.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import pl.karczmarczyk.ownplace.models.Plot;

/**
 *
 * @author mateusz
 */
public class PlotDTO {
    
    private static List<Plot> plots = new ArrayList<Plot>();
    
    public static List<Plot> getList () {
        return plots;
    }
    
    public static void updateList (ResultSet rs) {
        System.out.println("before update: "+getList().size());
        System.out.println("updateList");
        getList().clear();
        try {
            while (rs.next()) {
                addToList(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlotDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("after update: "+getList().size());
    }
    
    public static List<Plot> getMyPlotsByLocation (Location location, String userUuid) {
        int x = new Double(location.getX()).intValue();
        int y = new Double(location.getY()).intValue();
        int z = new Double(location.getZ()).intValue();
        return getMyPlotsByLocation(x, y, z, userUuid);
    }
    
    public static List<Plot> getPlotsByLocation (Location location) {
        int x = new Double(location.getX()).intValue();
        int y = new Double(location.getY()).intValue();
        int z = new Double(location.getZ()).intValue();
        return getPlotsByLocation(x, y, z);
    }
    
    public static List<Plot> getPlotsByLocation (int x, int y, int z) {
        return getMyPlotsByLocation(x, y, z, null);
    }
    
    public static List<Plot> getMyPlotsByLocation (int x, int y, int z, String userUuid) {
        List<Plot> results = new ArrayList<Plot>();
        for (Plot plot:getList()) {
            if (!plot.getIsActive()) {
                continue;
            }
            if (null!=userUuid && plot.getUserUuid().equals(userUuid) && plot.contains(x, y, z)) {
                results.add(plot);
            } else if (null==userUuid && plot.contains(x, y, z)) {
                results.add(plot);
            }
        }
        System.out.println("All plots on list: "+getList().size());
        System.out.println("Selected plots list: "+results.size());
        return results;
    }
    
    public static List<Plot> getMyPlots(String userUuid) {
        List<Plot> results = new ArrayList<Plot>();
        for (Plot plot:getList()) {
            if (!plot.getIsActive()) {
                continue;
            }
            if (null!=userUuid && plot.getUserUuid().equals(userUuid)) {
                results.add(plot);
            }
        }
        return results;
    }
    
    public static List<Plot> getUnactivePlots () {
        List<Plot> results = new ArrayList<Plot>();
        for (Plot plot:getList()) {
            if (!plot.getIsActive()) {
                results.add(plot);
            }
        }
        return results;
    }
    
    private static void addToList(ResultSet rs) throws SQLException {
        Plot plot = new Plot();
        plot.setId(rs.getInt("id"));
        plot.setX1(rs.getInt("x1"));
        plot.setZ1(rs.getInt("z1"));
        plot.setX2(rs.getInt("x2"));
        plot.setZ2(rs.getInt("z2"));
        plot.setArea(rs.getInt("area"));
        plot.setUserUuid(rs.getString("owner"));
        plot.setDescription(rs.getString("description"));
        plot.setIsActive(rs.getInt("is_active")==1?true:false);
        getList().add(plot);
        System.out.print(plot.toString());
    }
}
