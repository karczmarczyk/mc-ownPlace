package pl.karczmarczyk.ownplace;

import org.bukkit.plugin.java.JavaPlugin;
import pl.karczmarczyk.ownplace.listeners.OwnDoorListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.karczmarczyk.db.SQLiteConnection;
import pl.karczmarczyk.ownplace.commands.CommandFactory;
import pl.karczmarczyk.ownplace.dto.PlotDTO;
import pl.karczmarczyk.ownplace.listeners.PlotListener;

/**
 *
 * @author mateusz
 */
public final class OwnPlace extends JavaPlugin {

    public SQLiteConnection sqlite;
    public PlotDTO plotDTO;

    @Override
    public void onEnable() {
        getLogger().info("is running!");
        // Ini db conection
        sqlite = new SQLiteConnection(this,
            "OwnPlace",
            getDataFolder().getAbsolutePath(),
            "own_doors");
        getLogger().info("register OwnDoorListener!");
        
        //Ini Plot
        updatePlotDTO();
        
        // Ini Listeners
        new OwnDoorListener(this);
        new PlotListener(this);
    }

    @Override
    public void onDisable() {
        getConnection().close(); 
        getLogger().info("is stoped!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        getLogger().info("onCommand:"+cmd.getName());
        if (cmd.getName().equalsIgnoreCase("own_place") && args.length >= 1) { 

            CommandFactory.createCommand(this, sender, args);
            return true;
        } 

        return false; 
    }
    
    /* ==================================================================== */
    
    /**
     * Zwraca połączenie
     * @return 
     */
    public SQLiteConnection getConnection() {
        return sqlite;
    }
    
    /* ==================================================================== */
    
    /**
     * Zwraca obiekt dzialek
     * @return 
     */
    public PlotDTO getPlotDTO () {
        return plotDTO;
    }
    
    public void updatePlotDTO () {
        getPlotDTO().updateList(getConnection().getPlots());
    }

}
