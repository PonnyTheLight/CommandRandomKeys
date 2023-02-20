package es.ponnythelight.commandrandomkeys118.utils;

import es.ponnythelight.commandrandomkeys118.CommandRandomKeys;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class KeysManager {

    private CommandRandomKeys plugin;
    public KeysManager(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }

    public String GenerateNewKey(String args1, String args2, int args4, String args3) {
        FileConfiguration config = plugin.getConfig();

        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < config.getInt("Config.code-char-length")) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        String generatedKey = salt.toString();

        FileConfiguration keysc = plugin.getKeysConfig();

        keysc.set("Keys."+generatedKey+".uses", args4);
        keysc.set("Keys."+generatedKey+".used", 0);

        int seconds = convertCooldownToSeconds(args2);

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        keysc.set("Keys."+generatedKey+".createdAt", date);

        String sourceDate = date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = null;

        try {
            myDate = format.parse(sourceDate);
            String newDateString = format.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myDate = addDays(myDate, seconds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date2 = df.format(myDate);


        keysc.set("Keys."+generatedKey+".expireDate", date2);
        keysc.set("Keys."+generatedKey+".creatorNick", args3);

        keysc.set("Keys."+generatedKey+".commandGroup", args1);


        plugin.saveKeysConfig();
        plugin.reloadKeysConfig();

        if (config.getBoolean("Discord.enabled")) {
            DiscordWebhooks webhookdc = new DiscordWebhooks(config.getString("Discord.created-key-webhook"));
            webhookdc.setUsername(args3);
            webhookdc.setContent("asd");
            DiscordWebhooks.EmbedObject embed = new DiscordWebhooks.EmbedObject();
            embed.setTitle("NEW KEY CREATED!");
            embed.setDescription("Key: `%key%`\nUses: `%uses%`\nCreated Date: `%createdate%`\nExpire Date: `%expireDate%`\nCommands Group: `%commandsGroup%`\n Created By: `%creator%`"
                    .replaceAll("%key%", generatedKey)
                    .replaceAll("%uses%", String.valueOf(args4))
                    .replaceAll("%creator%", args3)
                    .replaceAll("%createdate%", date)
                    .replaceAll("%expireDate%", date2)
                    .replaceAll("%commandsGroup%", args1));
            embed.setColor(Color.RED);
            embed.setFooter("CommandRandomKeys | Logs", null);
            webhookdc.addEmbed(embed);
            try {
                webhookdc.execute();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return generatedKey;
    }

    public boolean deleteKey(String key, int i) {
        FileConfiguration keyc = plugin.getKeysConfig();
        FileConfiguration config = plugin.getConfig();

        if (i == 1) {
            ConfigurationSection keyss = keyc.getConfigurationSection("Keys");

            keyss.getKeys(false).forEach(k -> {
                ConfigurationSection keyd = keyc.getConfigurationSection("Keys."+k);

                keyss.set(k, null);
            });

            plugin.saveKeysConfig();
            plugin.reloadKeysConfig();
            return true;
        } else {
            if (keyc.contains("Keys." + key)) {
                ConfigurationSection keyd = keyc.getConfigurationSection("Keys."+key);
                ConfigurationSection keyss = keyc.getConfigurationSection("Keys");
                keyss.set(key, null);

                plugin.saveKeysConfig();
                plugin.reloadKeysConfig();
                return true;
            } else {
                return false;
            }
        }
    }

    public static Date addDays(Date date, int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static int convertCooldownToSeconds(String str) {
        int cooldownTime = 0;

        String[] split = str.split(" ");

        for (String time : split) {
            char key = time.charAt(1);
            int value = Integer.parseInt("" + time.charAt(0));

            if (time.length() >= 3) {
                key = time.charAt(2);
                value = Integer.parseInt("" + time.charAt(0) + time.charAt(1));
            }
            if (key == 'y') {
                cooldownTime += value * 31536000;
            }
            if (key == 'M') {
                cooldownTime += value * 2628000;
            }
            if (key == 'w') {
                cooldownTime += value * 604800;
            }
            if (key == 'd') {
                cooldownTime += value * 86400;
            }
            if (key == 'h') {
                cooldownTime += value * 3600;
            }
            if (key == 'm') {
                cooldownTime += value * 60;
            }
            if (key == 's') {
                cooldownTime += value;
            }


        }

        return cooldownTime;
    }
}
