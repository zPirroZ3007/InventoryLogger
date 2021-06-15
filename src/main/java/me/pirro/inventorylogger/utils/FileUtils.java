package me.pirro.inventorylogger.utils;

import lombok.Getter;
import me.pirro.inventorylogger.Main;
import me.pirro.inventorylogger.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 06 aprile, 2020
 */
public class FileUtils {
    @Getter
    private static File dataFolder = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/data/");
    @Getter
    public static YamlConfiguration config;
    public static File configFile = new File(Main.getInstance().getDataFolder(), "config.yml");

    @Getter
    public static YamlConfiguration messages;
    public static File messagesFile = new File(Main.getInstance().getDataFolder(), "messages.yml");

    public static void setup() {
        setupConfigs();
    }

    private static void setupConfigs() {
        if (!Main.getInstance().getDataFolder().exists())
            Main.getInstance().getDataFolder().mkdir();
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        if (!configFile.exists()) {
            try (InputStream in = Main.getInstance().getResource("config.yml")) {
                java.nio.file.Files.copy(in, configFile.toPath());
                Bukkit.getLogger().log(Level.WARNING, "§e[" + Main.getInstance().getName() + "] The file §fconfig.yml§e has been created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!messagesFile.exists()) {
            try (InputStream in = Main.getInstance().getResource("messages.yml")) {
                java.nio.file.Files.copy(in, messagesFile.toPath());
                Bukkit.getLogger().log(Level.WARNING, "§e[" + Main.getInstance().getName() + "] The file §fmessages.yml§e has been created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        Config.SAVE_EVERY = getConfig().getLong("save-every");
        Config.LOG_EMPTY_INVENTORIES = getConfig().getBoolean("log-empty-inventories");
        Config.DEBUG = getConfig().getBoolean("debug");
        Config.LOG_ON_DEATH = getConfig().getBoolean("log-on.death");
        Config.LOG_ON_WORLD_CHANGE = getConfig().getBoolean("log-on.world-change");
        Config.LOG_ON_QUIT = getConfig().getBoolean("log-on.quit");
        Config.LOG_ON_JOIN = getConfig().getBoolean("log-on.join");
        Config.LOG_ENABLED = getConfig().getBoolean("logger-enabled");
        Config.CLEAN_OLD_LOGS_AUTO = getConfig().getBoolean("remove-old-logs-automatically");
        Config.OLD_TIME = getConfig().getLong("old-cleanup") * 24 * 60 * 60 * 1000;

        Bukkit.getLogger().log(Level.INFO, "§a[" + Main.getInstance().getName() + "] §7The file §fconfig.yml§7 has been loaded.");
        Bukkit.getLogger().log(Level.INFO, "§a[" + Main.getInstance().getName() + "] §7The file §fmessages.yml§7 has been loaded.");
    }

    public static void ReloadConfigs() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            Config.SAVE_EVERY = getConfig().getLong("save-every");
            Config.LOG_EMPTY_INVENTORIES = getConfig().getBoolean("log-empty-inventories");
            Config.DEBUG = getConfig().getBoolean("debug");

            Config.LOG_ON_DEATH = getConfig().getBoolean("log-on.death");
            Config.LOG_ON_WORLD_CHANGE = getConfig().getBoolean("log-on.world-change");
            Config.LOG_ON_QUIT = getConfig().getBoolean("log-on.quit");
            Config.LOG_ON_JOIN = getConfig().getBoolean("log-on.join");
            Config.LOG_ENABLED = getConfig().getBoolean("logger-enabled");
            Config.CLEAN_OLD_LOGS_AUTO = getConfig().getBoolean("remove-old-logs-automatically");
			Config.OLD_TIME = getConfig().getLong("old-cleanup") * 24 * 60 * 60 * 1000;

            Bukkit.getScheduler().cancelTasks(Main.getInstance());
            Util.startThreads();

            messages = YamlConfiguration.loadConfiguration(messagesFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            config.save(configFile);
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveMessages() {
        try {
            messages.save(messagesFile);
            messages = YamlConfiguration.loadConfiguration(messagesFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}