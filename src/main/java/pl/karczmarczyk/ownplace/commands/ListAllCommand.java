package pl.karczmarczyk.ownplace.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

/**
 *
 * @author mateusz
 */
public class ListAllCommand extends CommandBase implements CommandInterface {

    @Override
    public Boolean check() {
        return true;
    }

    @Override
    public void run() {
        //@todo: dodać paginację
        Player player = (Player) sender;
        ResultSet rs = plugin.getConnection().getObjList(player.getUniqueId().toString());
        try {
            int i = 1;
            while (rs.next()) {
                sender.sendMessage(i+". ID:"+rs.getString("id")+" (XYZ: "+rs.getString("x")+" / "+rs.getString("y")+" / "+rs.getString("z")+")");
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListAllCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
