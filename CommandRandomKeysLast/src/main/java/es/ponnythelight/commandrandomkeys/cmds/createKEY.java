package es.ponnythelight.commandrandomkeys.cmds;

import es.ponnythelight.commandrandomkeys.CommandRandomKeys;
import es.ponnythelight.commandrandomkeys.utils.DiscordWebhooks;
import es.ponnythelight.commandrandomkeys.utils.KeysManager;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class createKEY implements CommandExecutor {
    public CommandRandomKeys plugin;

    public createKEY(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        KeysManager keys = new KeysManager(plugin);
        FileConfiguration messages = plugin.getMessagesConfig();
        FileConfiguration config = plugin.getConfig();




        if (sender instanceof Player) {
            Player pl = (Player) sender;

            if (pl.hasPermission("cmdrandomkeys.create")) {
                if (args.length <2 || args.length >3) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.ck-missing-args")));
                } else {
                    if (config.contains("commandsGroups."+args[0])) {
                        if (args.length == 3) {
                            String keygenerated = keys.GenerateNewKey(args[0], args[1], Integer.parseInt(args[2]), pl.getName());
                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-generated")
                                    .replaceAll("%key%", keygenerated)
                                    .replaceAll("%uses%", args[2])
                                    .replaceAll("%expiretime%", args[1])));

                            BaseComponent[] messagee = color(messages.getString("Messages.key-generated-copy"));
                            TextComponent messagea = new TextComponent(messagee);

                            messagea.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, keygenerated));
                            messagea.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("Messages.hover-copy")).create()));
                            sender.spigot().sendMessage(messagea);
                        } else {
                            String keygenerated = keys.GenerateNewKey(args[0], args[1], 1, pl.getName());

                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-generated")
                                    .replaceAll("%key%", keygenerated)
                                    .replaceAll("%uses%", "1")
                                    .replaceAll("%expiretime%", args[1])));

                            BaseComponent[] messagee = color(messages.getString("Messages.key-generated-copy"));
                            TextComponent messagea = new TextComponent(messagee);

                            messagea.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, keygenerated));
                            messagea.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("Messages.hover-copy")).create()));
                            sender.spigot().sendMessage(messagea);
                        }
                    } else {
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.no-commandGroup-admin")
                                .replaceAll("%commandGroup%", args[0])));
                    }
                }
            } else {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX") + messages.getString("Messages.no-perms")));
            }
        } else {
            ConsoleCommandSender console = plugin.getServer().getConsoleSender();

                if (args.length <2 || args.length >3) {
                    console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.ck-missing-args")));
                } else {
                    if (config.contains("commandsGroups."+args[0])) {
                        if (args.length == 3) {
                            String keygenerated = keys.GenerateNewKey(args[0], args[1], Integer.parseInt(args[2]), "CONSOLE");
                            console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-generated")
                                    .replaceAll("%key%", keygenerated)
                                    .replaceAll("%uses%", args[2])
                                    .replaceAll("%expiretime%", args[1])));

                            BaseComponent[] messagee = color(messages.getString("Messages.key-generated-copy"));
                            TextComponent messagea = new TextComponent(messagee);

                            messagea.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, keygenerated));
                            messagea.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("Messages.hover-copy")).create()));
                            sender.spigot().sendMessage(messagea);
                        } else {
                            String keygenerated = keys.GenerateNewKey(args[0], args[1], 1, "CONSOLE");

                            console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.key-generated")
                                    .replaceAll("%key%", keygenerated)
                                    .replaceAll("%uses%", "1")
                                    .replaceAll("%expiretime%", args[1])));

                            BaseComponent[] messagee = color(messages.getString("Messages.key-generated-copy"));
                            TextComponent messagea = new TextComponent(messagee);

                            messagea.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, keygenerated));
                            messagea.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("Messages.hover-copy")).create()));
                            sender.spigot().sendMessage(messagea);
                        }
                    } else {
                        console.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PREFIX")+messages.getString("Messages.no-commandGroup-admin")
                                .replaceAll("%commandGroup%", args[0])));
                    }
                }
        }

        return false;
    }

    public static BaseComponent[] color(String text) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', text));
    }
}
