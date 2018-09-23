package pl.karczmarczyk.ownplace.commands;

import org.bukkit.command.CommandSender;
import pl.karczmarczyk.ownplace.OwnPlace;

/**
 *
 * @author mateusz
 */
public interface CommandInterface {
    public void conf(OwnPlace plugin, CommandSender sender, String[] args);
    public Boolean check();
    public void run();
    public void onBadConfig() throws Exception;
    public String getValidMessage ();
}
