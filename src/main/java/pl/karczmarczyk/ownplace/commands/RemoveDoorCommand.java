package pl.karczmarczyk.ownplace.commands;

import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

/**
 *
 * @author mateusz
 */
public class RemoveDoorCommand extends CommandBase implements CommandInterface {

    @Override
    public Boolean check() {
        if(this.args.length<2) {
            this.validMessage = "command: \\own_place remove_door {ID}";
            return false;
        }
        if(!StringUtils.isNumeric(this.args[1])) {
            this.validMessage = "{ID} must by numeric";
            return false;
        }
        try {
            if (!checkIsOwner(Integer.valueOf(args[1]))) {
                this.validMessage = "Nie możesz deaktywować nieswoich drzwi!";
                return false;
            }
        } catch (SQLException ex) {
            plugin.getLogger().info("ERROR during check:"+ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        Player player = (Player) sender;
        plugin.getConnection().deleteObj(Integer.valueOf(args[1]), player.getUniqueId().toString());
        sender.sendMessage("Drzwi zostały deaktywowane!");
    }
}
