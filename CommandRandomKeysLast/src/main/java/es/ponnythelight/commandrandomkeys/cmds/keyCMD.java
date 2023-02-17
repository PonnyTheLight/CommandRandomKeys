package es.ponnythelight.commandrandomkeys.cmds;

import es.ponnythelight.commandrandomkeys.CommandRandomKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class keyCMD implements CommandExecutor {

    public CommandRandomKeys plugin;

    public keyCMD(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration messages = plugin.getMessagesConfig();
        FileConfiguration keys = plugin.getKeysConfig();
        FileConfiguration config = plugin.getConfig();

        if (sender instanceof Player) {
            Player pl = (Player) sender;

            if (args.length < 1 || args.length > 1) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.k-missing-args")));
            } else {
                if (keys.contains("Keys." + args[0])) {

                } else {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-missing")));
                }
            }

        } else {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.console")));
        }
        return false;
    }
}
