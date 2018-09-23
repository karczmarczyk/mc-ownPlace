package pl.karczmarczyk.ownplace.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pl.karczmarczyk.ownplace.OwnPlace;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author mateusz
 */
public final class OwnDoorListener implements Listener {

    OwnPlace instance;
    final String metadataName = "owner";
    final int materialListDoors[] = {
        Material.WOODEN_DOOR.getId(),
        Material.SPRUCE_DOOR.getId(),
        Material.BIRCH_DOOR.getId(),
        Material.JUNGLE_DOOR.getId(),
        Material.ACACIA_DOOR.getId(),
        Material.DARK_OAK_DOOR.getId()
    //    Material.IRON_DOOR_BLOCK.getId()
    };
    final boolean debugMode = true;

    public OwnDoorListener(OwnPlace plugin) {
        instance = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlaced(BlockPlaceEvent event) {
        debugLog("====>onPlaced");
        Block block = event.getBlock();
        Player player = event.getPlayer();
        debugLog("OnList:"+String.valueOf(Material.WOODEN_DOOR.getId()));
        debugLog("block.getType():"+block.getType().toString());
        debugLog("block.getType().name()):"+block.getType().name());
        debugLog("block.getType().getId():"+String.valueOf(block.getType().getId()));
        if (null!=block && null!=block.getType() && isInList(block.getType().getId(), materialListDoors)) {
            instance.getConnection().addObj(block.getLocation(), player.getUniqueId().toString());
            debugLog("Set owner: " + player.getUniqueId().toString());
            player.sendMessage("Zbudowano drzwi o ID:"+instance.getConnection().getLastId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        debugLog("====>onInteract");
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        debugLog("player: "+player.getUniqueId().toString());
        Boolean is = true;
        
        if (null!=block && null!=block.getType() && isInList(block.getType().getId(), materialListDoors)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ResultSet result = instance.getConnection().getObj(block.getLocation());
                try {
                    if (null!=result && result.next()) {
                        is = false;
                        if (canOpen(result, player)) {
                            is = true;
                        }
                        player.sendMessage("Drzwi o ID:"+result.getInt("id"));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(OwnDoorListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        if (!is) {
            debugLog("canceling!");
            event.setCancelled(true);
            player.sendMessage("To nie są twoje drzwi!");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDestroy(BlockBreakEvent event) {
        debugLog("====>onDestroy");
        Block block = event.getBlock();
        Player player = event.getPlayer();
        debugLog("player: "+player.getUniqueId().toString());
        Boolean is = true;
        
        if (null!=block && null!=block.getType() && isInList(block.getType().getId(), materialListDoors)) {
            ResultSet result = instance.getConnection().getObj(block.getLocation());
            try {
                if (null!=result && result.next()) {
                    is = false;
                    if (result.getString("owner").equals(player.getUniqueId().toString())) {
                        is = true;
                        debugLog("destroing your door!");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(OwnDoorListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (!is) {
            debugLog("canceling!");
            event.setCancelled(true);
            player.sendMessage("To nie są twoje drzwi!");
        }
    }
    
    private boolean isInList(int target, int[] list){
        if(list == null) return(false);
        for(int x = 0; x < list.length; ++x) if(target == list[x]) return(true);
        return(false);
    }

    private void debugLog(String msg) {
        if(debugMode) {
            instance.getLogger().info(msg);
        }
    }
    
    private boolean canOpen (ResultSet row, Player player) throws SQLException {
        if (row.getString("owner").equals(player.getUniqueId().toString())) {
            debugLog("Open your door!");
            return true;
        }
        ResultSet rs = instance.getConnection().getSharedFor(row.getInt("id"),
                player.getUniqueId().toString());
        if (rs.next()) {
            debugLog("Open shared door!");
            return true;
        }
        return false;
    }
}
