package es.ponnythelight.commandrandomkeys118.cmds;

import es.ponnythelight.commandrandomkeys118.utils.KeysManager;
import es.ponnythelight.commandrandomkeys118.CommandRandomKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class deleteKEY implements CommandExecutor {
    public CommandRandomKeys plugin;

    public deleteKEY(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        KeysManager keys = new KeysManager(plugin);
        FileConfiguration messages = plugin.getMessagesConfig();
        FileConfiguration config = plugin.getConfig();


        if (sender instanceof Player) {
            Player pl = (Player) sender;

            if (pl.hasPermission("cmdrandomkeys.delete")) {
                if (args.length == 0) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.dk-missing-args")));
                } else {
                    if (args[0].equalsIgnoreCase("all") ) {
                        Boolean deleted = keys.deleteKey(args[0], 1);

                        if (deleted) {
                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.all-keys-deleted")));
                        }
                    } else {
                        FileConfiguration keyss = plugin.getKeysConfig();
                        if (keyss.contains("Keys." + args[0])) {
                            Boolean deleted = keys.deleteKey(args[0], 0);

                            if (deleted) {
                                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.key-deleted")
                                        .replaceAll("%key%", args[0])));
                            }
                        } else {
                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.key-missing")));
                        }
                    }
                }
            } else {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.no-perms")));
            }
        } else {
            ConsoleCommandSender console = plugin.getServer().getConsoleSender();

            if (args.length == 0) {
                console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.dk-missing-args")));
            } else {
                if (args[0].equalsIgnoreCase("all") ) {
                    Boolean deleted = keys.deleteKey(args[0], 1);

                    if (deleted) {
                        console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.all-keys-deleted")));
                    }
                } else {
                    FileConfiguration keyss = plugin.getKeysConfig();
                    if (keyss.contains("Keys." + args[0])) {
                        Boolean deleted = keys.deleteKey(args[0], 0);

                        if (deleted) {
                            console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.key-deleted")
                                    .replaceAll("%key%", args[0])));
                        }
                    } else {
                        console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.key-missing")));
                    }
                }
            }

        }

        return false;
    }
}
