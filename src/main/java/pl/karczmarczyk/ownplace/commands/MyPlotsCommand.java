package pl.karczmarczyk.ownplace.commands;

import java.util.List;
import org.bukkit.entity.Player;
import pl.karczmarczyk.ownplace.models.Plot;

/**
 *
 * @author mateusz
 */
public class MyPlotsCommand extends CommandBase implements CommandInterface {

    @Override
    public Boolean check() {
        return true;
    }

    @Override
    public void run() {
        //@todo: dodać paginację
        Player player = (Player) sender;
        List<Plot> list = plugin.getPlotDTO().getMyPlots(player.getUniqueId().toString());
        int i = 1;
        for (Plot plot:list) {
            sender.sendMessage(i+". ID:"+plot.getId()+" (P1 - P2: "+plot.getX1()+" / "+plot.getZ1()+" - "+plot.getX2()+" / "+plot.getZ2());
            i++;
        }
    }
    
}
