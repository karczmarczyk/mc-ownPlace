package pl.karczmarczyk.ownplace.listeners;

import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import pl.karczmarczyk.ownplace.OwnPlace;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import pl.karczmarczyk.ownplace.dto.PlotDTO;
import pl.karczmarczyk.ownplace.models.Plot;

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
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlaced(BlockPlaceEvent event) {
        debugLog("====>PlotListener onPlaced");
        Player player = event.getPlayer();
        if (!can(event.getBlock(), player)) {
            event.setCancelled(true);
            player.sendMessage("To jest działka prywatna i nie możesz tu budować!");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDestroy(BlockBreakEvent event) {
        debugLog("====>PlotListener onDestroy");
        Player player = event.getPlayer();
        if (!can(event.getBlock(), player)) {
            event.setCancelled(true);
            player.sendMessage("To jest działka prywatna i nie możesz tu nic niszczyć!");
        }
    }
    
    private boolean can(Block block, Player player) {
        List<Plot> plots = instance.getPlotDTO().getPlotsByLocation(block.getLocation());
        if (plots.size()>0) {
            for (Plot plot:plots) {
                if (!hasPerm(plot, player)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean hasPerm (Plot plot, Player player) {
        debugLog("hasPerm for: "+player.getUniqueId().toString());
        if (plot.getUserUuid().equals(player.getUniqueId().toString())) {
            debugLog("hasn't perm!");
            return true;
        }
        return false;
    }
    
    private void debugLog(String msg) {
        if(debugMode) {
            instance.getLogger().info(msg);
        }
    }
}
