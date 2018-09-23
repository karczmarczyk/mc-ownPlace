package pl.karczmarczyk.ownplace.commands;

import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

/**
 *
 * @author mateusz
 */
public class UnshareDoorCommand extends CommandBase implements CommandInterface {
    @Override
    public Boolean check() {
        if(this.args.length<3) {
            this.validMessage = "command: \\own_place unshare_door {ID} {UserName}";
            return false;
        }
        if(!StringUtils.isNumeric(this.args[1])) {
            this.validMessage = "{ID} must by numeric";
            return false;
        }
        if (null==getPlayerUuidByName(args[2]) && !"-all".equals(args[2])) {
            this.validMessage = "Gracz o nazwie '"+args[2]+"' jest niedostępny.";
            return false;
        }
        try {
            if (!checkIsOwner(Integer.valueOf(args[1]))) {
                this.validMessage = "Nie możesz wyłączyć udostępnianie nieswoich drzwi!";
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
        if (!"-all".equals(args[2])) {
            plugin.getConnection().unshareObj(Integer.valueOf(args[1]), 
                    getPlayerUuidByName(args[2]));
            sender.sendMessage("Udostępnianie tych drzwi zostało wyłączone!");
        } else if ("-all".equals(args[2])) {
            //@todo?
        }
    }
    
}
