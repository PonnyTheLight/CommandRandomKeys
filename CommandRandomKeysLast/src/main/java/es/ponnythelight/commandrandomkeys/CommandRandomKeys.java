package es.ponnythelight.commandrandomkeys;

import es.ponnythelight.commandrandomkeys.cmds.createKEY;
import es.ponnythelight.commandrandomkeys.cmds.deleteKEY;
import es.ponnythelight.commandrandomkeys.cmds.keyCMD;
import es.ponnythelight.commandrandomkeys.cmds.mainCMD;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class CommandRandomKeys extends JavaPlugin {
    PluginDescriptionFile desc;
    String spigoturl = "https://www.spigotmc.org/resources/chunkentitylimiter-1-13-limit-entities-and-dropped-items-per-chunk.108040/";

    private File messagesFile;
    private FileConfiguration messagesConfig;

    private File keysFile;
    private FileConfiguration keysConfig;

    @Override
    public void onEnable() {
        desc = getDescription();
        registerCommands();
        saveDefaultConfig();
        createKeysConfig();
        createMessagesConfig();
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eRandomKeys&8] &aRandomKeys enabled successful."));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eRandomKeys&8] &cRandomKeys disabled successful."));
    }

    public void registerCommands() {
        getCommand("randomkeys").setExecutor(new mainCMD(this));
        getCommand("createkey").setExecutor(new createKEY(this));
        getCommand("key").setExecutor(new keyCMD(this));
        getCommand("deletekey").setExecutor(new deleteKEY(this));
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
    }

    public FileConfiguration getMessagesConfig() {
        return this.messagesConfig;
    }

    public boolean reloadMessagesConfig() {
        loadYaml(messagesFile, messagesConfig);
        return true;
    }
    private void createMessagesConfig() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getKeysConfig() {
        return this.keysConfig;
    }

    public boolean reloadKeysConfig() {
        loadYaml(keysFile, keysConfig);
        return true;
    }

    public void loadYaml(File file, FileConfiguration configuration) {
        try {
            configuration.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveKeysConfig() {
        try {
            keysConfig.save(keysFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createKeysConfig() {
        keysFile = new File(getDataFolder(), "keys.yml");
        if (!keysFile.exists()) {
            keysFile.getParentFile().mkdirs();
            saveResource("keys.yml", false);
        }

        keysConfig = new YamlConfiguration();
        try {
            keysConfig.load(keysFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public PluginDescriptionFile getDesc() {
        return desc;
    }
    public String getSpigotURL() {
        return spigoturl;
    }
}
