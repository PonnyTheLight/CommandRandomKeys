package es.ponnythelight.commandrandomkeys118.cmds;

import es.ponnythelight.commandrandomkeys118.CommandRandomKeys;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class mainCMD implements CommandExecutor {
    private final CommandRandomKeys plugin;
    public mainCMD(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration keys = plugin.getKeysConfig();
        FileConfiguration messages = plugin.getMessagesConfig();
        PluginDescriptionFile desc = plugin.getDesc();
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            if (args.length == 0) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + "&cUse &f/commandrandomkeys help &cto see all plugin commands. Use &f/commadrandomkeys info &cto see the plugin information."));
            } else if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("checkkey")) {
                if (pl.hasPermission("cmdrandomkeys.check")) {
                    if (args.length > 1) {
                        if (keys.contains("Keys." + args[1])) {
                            ConfigurationSection code = keys.getConfigurationSection("Keys." + args[1]);


                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime d1 = LocalDateTime.now();
                            LocalDateTime d2 = LocalDateTime.parse(code.getString("expireDate"), formatter);
                            long diff = ChronoUnit.SECONDS.between(d1, d2);

                            String expired;

                            if (diff > 0) {
                                expired = "No";
                            } else {
                                expired = "Yes";
                            }

                            int uses = code.getInt("uses");
                            int used = code.getInt("used");
                            int usesreaming = uses - used;

                            List<String> msgs = messages.getStringList("Messages.check-key");

                            for (String m : msgs) {
                                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', m
                                        .replaceAll("%expired%", expired)
                                        .replaceAll("%usesreaming%", String.valueOf(usesreaming))
                                        .replaceAll("%createddate%", code.getString("createdAt"))
                                        .replaceAll("%expiredate%", code.getString("expireDate"))
                                        .replaceAll("%commandgroup%", code.getString("commandGroup"))
                                        .replaceAll("%key%", args[1])
                                        .replaceAll("%redeemedby%", stringJoiner(code.getStringList("RedeemedBy")))
                                ));
                            }

                        } else {
                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.key-missing")));
                        }
                    } else {
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.check-missing-args")));
                    }
                } else {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.no-perms")));
                }
            } else if (args[0].equalsIgnoreCase("keys") || args[0].equalsIgnoreCase("allkeys")) {
                if (pl.hasPermission("cmdrandomkeys.keys")) {
                    Set<String> keyss = keys.getConfigurationSection("Keys").getKeys(false);

                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.see-all-keys")));

                    if (args.length >= 2) {
                        paginate(sender, keyss, Integer.parseInt(args[1]), KeysCount(keyss), 1, pl);
                    } else {
                        paginate(sender, keyss, 1, KeysCount(keyss), 1, pl);
                    }
                } else {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.no-perms")));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (pl.hasPermission("cmdrandomkeys.reload")) {


                    plugin.reloadConfig();
                    plugin.reloadKeysConfig();
                    plugin.reloadMessagesConfig();

                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.reload")));
                } else {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.no-perms")));
                }

            } else if (args[0].equalsIgnoreCase("mykeys") || args[0].equalsIgnoreCase("keysmy")) {
                if (pl.hasPermission("cmdrandomkeys.own.keys")) {
                    Set<String> keyss = keys.getConfigurationSection("Keys").getKeys(false);

                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.see-own-keys")));
                    if (args.length >= 2) {
                        paginate(sender, keyss, Integer.parseInt(args[1]), KeysCount(keyss), 2, pl);
                    } else {
                        paginate(sender, keyss, 1, KeysCount(keyss), 2, pl);
                    }
                } else {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.no-perms")));
                }
            } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("help")) {
                List<String> msgs = messages.getStringList("Messages.help-cmd");
                for (String m : msgs) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
                }
            } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("info")) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m----------&8[ &fInfo &eCommandRandomKeys &8]&8&m----------"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eVersion: &f"+desc.getVersion()));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAuthor: &fPonnyTheLight"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eWebsite: &f"+desc.getWebsite()+""));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eSpigotURL: &f"+plugin.getSpigotURL()+""));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m----------&8[ &fInfo &eCommandRandomKeys &8]&8&m----------"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8"));
            }
        }else {

        }

        return false;
    }

    public String stringJoiner(List<String> list) {
        StringJoiner joiner = new StringJoiner(",");
        list.forEach(item -> joiner.add(item.toString()));
        return joiner.toString();
    }
    public void paginate(CommandSender sender, Set<String> list, int page, int countAll, int cmd, Player pl)
    {
        FileConfiguration messages = plugin.getMessagesConfig();
        FileConfiguration keys = plugin.getKeysConfig();

        int MyPage = page;
        int contentLinesPerPage = 5;
        int totalPageCount = 1;

        if((list.size() % contentLinesPerPage) == 0)
        {
            if(list.size() > 0)
            {
                totalPageCount = list.size() / contentLinesPerPage;
            }
        }
        else
        {
            totalPageCount = (list.size() / contentLinesPerPage) + 1;
        }

        if(page <= totalPageCount)
        {

            if(list.isEmpty() || list == null)
            {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.all-keys-empty")));
            }
            else
            {
                int i = 0, k = 0;
                page--;

                for (String entry : list)
                {
                    k++;
                    if ((((page * contentLinesPerPage) + i + 1) == k) && (k != ((page * contentLinesPerPage) + contentLinesPerPage + 1)))
                    {
                        i++;
                        int useda = keys.getInt("Keys."+entry+".used");
                        int usesa = keys.getInt("Keys."+entry+".uses");
                        String creator = keys.getString("Keys."+entry+".creatorNick");

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime d1 = LocalDateTime.now();
                        LocalDateTime d2 = LocalDateTime.parse(keys.getString("Keys." + entry + ".expireDate"), formatter);
                        long diff = ChronoUnit.SECONDS.between(d1, d2);

                        if (usesa > useda) {
                            if (diff > 0) {
                                if (cmd == 1) {
                                    TextComponent keycopy = new TextComponent(entry);

                                    keycopy.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, entry));
                                    keycopy.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("Messages.hover-copy")).create()));

                                    pl.spigot().sendMessage(keycopy);
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.all-keys-reaming").replaceAll("%reaming%", String.valueOf(usesa - useda))));
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.all-keys-creator").replaceAll("%creator%", creator)));
                                } else if (cmd == 2) {
                                    if (creator.equalsIgnoreCase(sender.getName())) {
                                        TextComponent keycopy = new TextComponent(entry);

                                        keycopy.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, entry));
                                        keycopy.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("Messages.hover-copy")).create()));

                                        pl.spigot().sendMessage(keycopy);
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.all-keys-reaming").replaceAll("%reaming%", String.valueOf(usesa - useda))));
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.all-keys-creator").replaceAll("%creator%", creator)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            String paginatefirstline = null;

            if (cmd == 1) {
                paginatefirstline =  messages.getString("Messages.pages-keys").replaceAll("%page%", String.valueOf(MyPage)).replaceAll("%pages%", String.valueOf(totalPageCount))
                        .replaceAll("%cmd%", "/rk keys <page>");
            } else if (cmd == 2) {
                paginatefirstline =  messages.getString("Messages.pages-keys").replaceAll("%page%", String.valueOf(MyPage)).replaceAll("%pages%", String.valueOf(totalPageCount))
                        .replaceAll("%cmd%", "/rk mykeys <page>");
            } else {
                paginatefirstline = "Broken, contact plugin creator";
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', paginatefirstline));
        }
        else
        {
            sender.sendMessage(messages.getString("Messages.PREFIX") + messages.getString("Messages.missing-page").replaceAll("%pages%", String.valueOf(totalPageCount)));
        }
    }

    public int KeysCount(Set<String> keyss) {
        FileConfiguration messages = plugin.getMessagesConfig();
        FileConfiguration keys = plugin.getKeysConfig();

        int count = 0;

        for (String key : keyss) {
            int useda = keys.getInt("Keys."+key+".used");
            int usesa = keys.getInt("Keys."+key+".uses");
            String creator = keys.getString("Keys."+key+".creatorNick");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime d1 = LocalDateTime.now();
            LocalDateTime d2 = LocalDateTime.parse(keys.getString("Keys." + key + ".expireDate"), formatter);
            long diff = ChronoUnit.SECONDS.between(d1, d2);

            if (usesa > useda) {
                if (diff > 0) {
                    count++;
                }
            }
        }
        return count;
    }
}





