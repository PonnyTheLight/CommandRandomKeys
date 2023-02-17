package es.ponnythelight.commandrandomkeys.utils;

import es.ponnythelight.commandrandomkeys.CommandRandomKeys;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.Configuration;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class KeysManager {

    private CommandRandomKeys plugin;
    public KeysManager(CommandRandomKeys plugin) {
        this.plugin = plugin;
    }
    public String GenerateNewKey(String args1, String args2, String args3) {
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

        keysc.set("Keys."+generatedKey+".redeemed", false);

        int seconds = convertCooldownToSeconds(args2);
        plugin.getServer().getConsoleSender().sendMessage(String.valueOf(seconds));

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        keysc.set("Keys."+generatedKey+".createdAt", date);

        String sourceDate = date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = null;

        try {
            myDate = format.parse(sourceDate);
            String newDateString = format.format(myDate);
            plugin.getServer().getConsoleSender().sendMessage(newDateString);
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

        return generatedKey;
    }

    public void getKeyFromKeys(String key) {

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

        for(String time : split) {
            char key = time.charAt(1);
            int value = Integer.parseInt(""+time.charAt(0));

            if(time.length() >= 3) {
                key = time.charAt(2);
                value = Integer.parseInt(""+time.charAt(0) + time.charAt(1));
            }

            if(key == 'y') {
                cooldownTime += value * 31536000;
            }
            if(key == 'M') {
                cooldownTime += value * 2628000;
            }
            if(key == 'w') {
                cooldownTime += value * 604800;
            }
            if(key == 'd') {
                cooldownTime += value * 86400;
            }
            if(key == 'h') {
                cooldownTime += value * 3600;
            }
            if(key == 'm') {
                cooldownTime += value * 60;
            }
            if(key == 's') {
                cooldownTime += value;
            }
        }

        return cooldownTime;
    }
}
