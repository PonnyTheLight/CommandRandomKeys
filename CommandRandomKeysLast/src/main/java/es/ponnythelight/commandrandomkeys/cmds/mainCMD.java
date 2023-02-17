package es.ponnythelight.commandrandomkeys.cmds;

import es.ponnythelight.commandrandomkeys.CommandRandomKeys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

public class mainCMD implements CommandExecutor {
    private final CommandRandomKeys plugin;
    public mainCMD(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration keys = plugin.getKeysConfig();
        FileConfiguration messages = plugin.getMessagesConfig();
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            if (args.length == 0) {

            } else if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("checkkey")) {
                if (keys.contains("Keys." + args[0])) {
                    ConfigurationSection code = keys.getConfigurationSection("Keys." + args[0]);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime d1 = LocalDateTime.parse(code.getString("createdAt"), formatter);
                    LocalDateTime d2 = LocalDateTime.parse(code.getString("expireDate"), formatter);
                    long diff = ChronoUnit.SECONDS.between(d1, d2);
                    pl.sendMessage("Difference: " + diff);
                    String expired;
                    String redeemed;
                    if (diff > 0) {
                        expired = messages.getString("Others.no");
                    } else {
                        expired = messages.getString("Others.yes");
                    }

                    if (code.getBoolean("redeemed")) {
                        redeemed = messages.getString("Others.yes");
                    } else {
                        redeemed = messages.getString("Others.no");
                    }

                    List<String> msgs = messages.getStringList("Messages.check-key");

                    for (String m : msgs) {
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', m
                                .replaceAll("%expired%", expired)
                                .replaceAll("%redeemed%", redeemed)
                                .replaceAll("createddate", code.getString("createdAt"))
                                .replaceAll("%expiredate%", code.getString("expireDate"))
                                .replaceAll("%commandGroup%", code.getString("commandGroup"))
                                .replaceAll("%key%", args[0])
                        ));
                    }

                } else {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-missing")));
                }
            }
        }else {

        }

        return false;
    }
}
