package pl.karczmarczyk.ownplace.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import pl.karczmarczyk.ownplace.OwnPlace;

/**
 *
 * @author mateusz
 */
public class PlotListener implements Listener {
    
    OwnPlace instance;
    final boolean debugMode = true;
    
    public PlotListener(OwnPlace plugin) {
        instance = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlaced(BlockPlaceEvent event) {
        
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDestroy(BlockBreakEvent event) {
        
    }
    
    private void debugLog(String msg) {
        if(debugMode) {
            instance.getLogger().info(msg);
        }
    }
}
