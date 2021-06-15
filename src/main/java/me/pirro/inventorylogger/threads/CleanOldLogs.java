package me.pirro.inventorylogger.threads;

import me.pirro.inventorylogger.Main;
import me.pirro.inventorylogger.configs.Config;
import me.pirro.inventorylogger.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 05 luglio, 2020
 */
public class CleanOldLogs extends BukkitRunnable {
    @Override
    public void run() {
        if (!Config.CLEAN_OLD_LOGS_AUTO)
            return;

        if (Config.DEBUG)
            Bukkit.getLogger().log(Level.INFO, "[" + Main.getInstance().getName() + "] Removing old logs...");
        int i = 0;
        for (final File fileEntry : FileUtils.getDataFolder().listFiles()) {
            YamlConfiguration log = YamlConfiguration.loadConfiguration(fileEntry);

            if (System.currentTimeMillis() - log.getLong("date") > Config.OLD_TIME) {
                fileEntry.delete();
                i++;
            }
        }

        if (Config.DEBUG)
            if (i > 0)
                Bukkit.getLogger().log(Level.INFO, "[" + Main.getInstance().getName() + "] Removed " + i + " old logs!");
    }
}
