package es.ponnythelight.commandrandomkeys.cmds;

import es.ponnythelight.commandrandomkeys.CommandRandomKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

            if (pl.hasPermission("cmdrandomkeys.claim")) {
                if (args.length < 1 || args.length > 1) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.k-missing-args")));
                } else {
                    if (keys.contains("Keys." + args[0], true)) {
                        ConfigurationSection code = keys.getConfigurationSection("Keys." + args[0]);


                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime d1 = LocalDateTime.now();
                        LocalDateTime d2 = LocalDateTime.parse(code.getString("expireDate"), formatter);
                        long diff = ChronoUnit.SECONDS.between(d1, d2);
                        int uses = code.getInt("uses");
                        int used = code.getInt("used");

                        if (uses > used) {
                            if (diff > 0) {

                                String commandGroup = code.getString("commandGroup");

                                if (code.contains("RedeemedBy")) {
                                    List<String> redeemers = code.getStringList("RedeemedBy");

                                    if (redeemers.contains(pl.getName())) {
                                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-already-redeemed")
                                                .replaceAll("%key%", args[0])));
                                        return true;
                                    }
                                }

                                if (config.contains("commandsGroups."+commandGroup)) {
                                    ConfigurationSection section = config.getConfigurationSection("commandsGroups."+commandGroup);
                                    List<String> cmds = section.getStringList("commands");

                                    for (String cmd : cmds) {
                                        if (cmd.contains("[CONSOLE]")) {
                                            String mcmd = cmd.replace("[CONSOLE]", "").replaceFirst(" ", "");
                                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), mcmd);
                                            plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Console-Logs.command-executed")
                                                    .replaceAll("%key%", args[0])
                                                    .replaceAll("%cmd%", mcmd)
                                                    .replaceAll("%redeemer%", pl.getName())));
                                        } else if (cmd.contains("[PLAYER]")) {
                                            String mcmd = cmd.replace("[PLAYER]", "").replaceFirst(" ", "");
                                            pl.performCommand(mcmd);
                                            plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Console-Logs.command-executed")
                                                    .replaceAll("%key%", args[0])
                                                    .replaceAll("%cmd%", "/" + mcmd)
                                                    .replaceAll("%redeemer%", pl.getName())));
                                        }
                                    }
                                } else {
                                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.no-commandGroup")
                                            .replaceAll("%commandgroup%", commandGroup)));
                                    return true;
                                }

                                if (code.contains("RedeemedBy")) {
                                    List<String> redeemers = code.getStringList("RedeemedBy");

                                    redeemers.add(pl.getName());

                                    code.set("used", used + 1);
                                } else {
                                    List<String> redeemersa = new ArrayList<>();

                                    redeemersa.add(pl.getName());
                                    code.set("RedeemedBy", redeemersa);

                                    code.set("used", used + 1);
                                }

                                plugin.saveKeysConfig();
                                plugin.reloadKeysConfig();

                                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-redeemed")
                                        .replaceAll("%key%", args[0])));
                            } else {
                                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-expired")));
                            }
                        } else {
                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-uses-redeemed")));
                        }
                    } else {
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-missing")));
                    }
                }
            } else {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.no-perms")));
            }

        } else {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.console")));
        }
        return false;
    }
}
