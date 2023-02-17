package es.ponnythelight.commandrandomkeys.cmds;

import es.ponnythelight.commandrandomkeys.CommandRandomKeys;
import es.ponnythelight.commandrandomkeys.utils.KeysManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.ParseException;


public class createKEY implements CommandExecutor {
    public CommandRandomKeys plugin;

    public createKEY(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        KeysManager keys = new KeysManager(plugin);
        FileConfiguration messages = plugin.getMessagesConfig();

        if (sender instanceof Player) {
            Player pl = (Player) sender;

            if (args.length <2 || args.length >2) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.ck-missing-args")));
            } else {
                String keygenerated = keys.GenerateNewKey(args[0], args[1], pl.getName());
                pl.sendMessage(keygenerated);
            }
        } else {

        }

        return false;
    }
}
