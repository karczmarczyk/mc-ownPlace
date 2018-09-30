package pl.karczmarczyk.ownplace.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

/**
 *
 * @author mateusz
 */
public class AcceptPlotCommand extends CommandBase implements CommandInterface {

    @Override
    public Boolean check() {
        if (args.length < 3) {
            this.validMessage = "command: \\own_place accept_plot {ID}";
            return false;
        }
        if(!StringUtils.isNumeric(this.args[1])) {
            this.validMessage = "{ID} must by numeric";
            return false;
        }
        try {
            if (!sender.isOp() && !checkIsOwner(Integer.valueOf(args[1]))) {
                this.validMessage = "Nie możesz akceptować działek!";
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
        plugin.getConnection().acceptPlot(Integer.parseInt(args[1]));
        plugin.updatePlotDTO();
        sender.sendMessage("Działka została zaakceptowana.");
    }   
    
    protected boolean checkIsOwner (int plotId) throws SQLException {
        Player player = (Player) sender;
        ResultSet rs = plugin.getConnection().getObj(plotId, player.getUniqueId().toString());
        return rs.next();
    }
}
