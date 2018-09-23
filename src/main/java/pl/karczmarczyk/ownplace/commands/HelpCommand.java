package pl.karczmarczyk.ownplace.commands;

/**
 *
 * @author mateusz
 */
public class HelpCommand extends CommandBase implements CommandInterface {

    @Override
    public Boolean check() {
        return true;
    }

    @Override
    public void run() {
        sender.sendMessage("Available commands: list_all_doors, remove_door, share_door, unshare_door, list_shared, my_plots, add_plot, remove_plot");
        sender.sendMessage("Available commands (only OP): accept_plot");
    }
    
}
