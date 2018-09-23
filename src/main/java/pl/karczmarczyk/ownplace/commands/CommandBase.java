package pl.karczmarczyk.ownplace.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import pl.karczmarczyk.ownplace.OwnPlace;
import org.bukkit.Bukkit;

/**
 *
 * @author mateusz
 */
public class CommandBase {
    OwnPlace plugin;
    String[] args;
    CommandSender sender;
    String validMessage = null;
    
    public void conf(OwnPlace plugin, CommandSender sender, String[] args) {
        this.plugin = plugin;
        this.args = args;
        this.sender = sender;
    }
    
    public void onBadConfig() throws Exception {
        //nothing to do..
    }
    
    public String getValidMessage () {
        return validMessage;
    }
    
    protected boolean checkIsOwner (int doorId) throws SQLException {
        Player player = (Player) sender;
        ResultSet rs = plugin.getConnection().getObj(doorId, player.getUniqueId().toString());
        return rs.next();
    }
    
    protected String getPlayerUuidByName (String username) {
        Player player = getPlayerByName(username);
        return null!=player?player.getUniqueId().toString():null;
    }
    
    protected Player getPlayerByName (String username) {
        try {
            Player player = Bukkit.getPlayer(username);
            if (null==player) {
                OfflinePlayer offlinePlayer = (Player) Bukkit.getOfflinePlayer(username);
                if(offlinePlayer.hasPlayedBefore()) {
                    return (Player) offlinePlayer;
                }
            }
            return player;
        } catch (Exception e) {
            return null;
        }
    }
}
