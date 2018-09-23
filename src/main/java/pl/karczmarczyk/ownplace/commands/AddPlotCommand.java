package pl.karczmarczyk.ownplace.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

/**
 *
 * @author mateusz
 */
public class AddPlotCommand extends CommandBase implements CommandInterface {

    @Override
    public Boolean check() {
        if (args.length < 6) {
            this.validMessage = "command: \\own_place add_plot {UserName} {x1} {z1} {x2} {z2} {description[optional]}";
            return false;
        }
        if (null==getPlayerUuidByName(args[1])) {
            this.validMessage = "Gracz o nazwie '"+args[1]+"' jest niedostępny.";
            return false;
        }
        if(!StringUtils.isNumeric(this.args[2])) {
            this.validMessage = "{x1} must by numeric";
            return false;
        }
        if(!StringUtils.isNumeric(this.args[3])) {
            this.validMessage = "{z1} must by numeric";
            return false;
        }
        if(!StringUtils.isNumeric(this.args[4])) {
            this.validMessage = "{x2} must by numeric";
            return false;
        }
        if(!StringUtils.isNumeric(this.args[5])) {
            this.validMessage = "{z2} must by numeric";
            return false;
        }
        
        return true;
    }

    @Override
    public void run() {
        plugin.getConnection().addPlot(
                Integer.valueOf(args[2]),
                Integer.valueOf(args[3]), 
                Integer.valueOf(args[4]), 
                Integer.valueOf(args[5]), 
                getPlayerUuidByName(args[1]), 
                sender.isOp()?1:0,
                getDescriptionFromArgs());
        plugin.updatePlotDTO();
        if (!sender.isOp()) {
            this.validMessage = "Tylko OP może dodawać działki. Zostało dodane zgłoszenie o przydzielenie działki!";
        } else {
            this.validMessage = "Działka została dodana";
        }
        Player player = getPlayerByName(args[1]);
        if (null!=player) {
            player.sendMessage("Została do Ciebie przydzielona działka.");
        }
    }
    
    private String getDescriptionFromArgs () {
        int count = args.length;
        String desc = "";
        if (count > 6) {
            for (int i=6;i<count;i++) {
                desc +=args[i]+" ";
            }
        }
        return desc;
    }
    
}
