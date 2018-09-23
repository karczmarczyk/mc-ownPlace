package pl.karczmarczyk.ownplace.commands;

import java.util.HashMap;
import org.bukkit.command.CommandSender;
import pl.karczmarczyk.ownplace.OwnPlace;

/**
 *
 * @author mateusz
 */
public class CommandFactory {
    
    CommandSender sender;
        
    private HashMap<String,CommandInterface> classes = new HashMap<String,CommandInterface>();
    
    private HashMap<String,CommandInterface> classesList () {
        classes.put("help", new HelpCommand());
        classes.put("list_all_doors", new ListAllCommand());
        classes.put("remove_door", new RemoveDoorCommand());
        classes.put("share_door", new ShareDoorCommand());
        classes.put("unshare_door", new UnshareDoorCommand());
        classes.put("add_plot", new AddPlotCommand());
//        classes.put("remove_plot", );
//        classes.put("my_plots", );
//        classes.put("accept_plot", );
        // REMEMBER: to update HelpCommand
        return classes;
    }
    
    public static void createCommand(OwnPlace plugin, CommandSender sender, String[] args) {
        CommandFactory cb = new CommandFactory();
        cb.sender = sender;
        try {
            CommandInterface c = cb.getClass(args[0]);
            c.conf(plugin, sender, args);
            if (c.check()) {
                c.run();
            } else {
                if (null!=c.getValidMessage()) {
                    sender.sendMessage(c.getValidMessage());
                }
                c.onBadConfig();
            }
            
        } catch (Exception ex) {
            plugin.getLogger().info("ERROR:"+ex.getMessage());
        }
    }
    
    private CommandInterface getClass(String param) throws Exception {
        param = param.toLowerCase();
        if (classesList().containsKey(param)) {
            return classesList().get(param);
        } else {
            sender.sendMessage("Wrong command! Type help for more info.");
            throw new Exception("No class found for command: "+param);
        }
    }
    
    
}
