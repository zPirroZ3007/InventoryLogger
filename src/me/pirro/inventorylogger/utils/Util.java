package me.pirro.inventorylogger.utils;

import me.pirro.inventorylogger.Main;
import me.pirro.inventorylogger.commands.InventoryLogs;
import me.pirro.inventorylogger.configs.Config;
import me.pirro.inventorylogger.listeners.InventoryClick;
import me.pirro.inventorylogger.listeners.JoinEvent;
import me.pirro.inventorylogger.listeners.LogListener;
import me.pirro.inventorylogger.listeners.TabComplete;
import me.pirro.inventorylogger.threads.*;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class Util
{
	public static String VERSION_NEW;

	public static void enable()
	{
		long start = System.currentTimeMillis();
		FileUtils.setup();
		registerCommands();
		registerListeners();
		startThreads();
		enableMetrics();

		Bukkit.getLogger().log(Level.INFO, "§a[" + Main.getInstance().getName() + "] Enabled in " + (System.currentTimeMillis() - start) + "ms");
	}

	public static void disable()
	{
		long start = System.currentTimeMillis();
		Bukkit.getScheduler().cancelTasks(Main.getInstance());

		Bukkit.getLogger().log(Level.INFO, "§c[" + Main.getInstance().getName() + "] Disabled in " + (System.currentTimeMillis() - start) + "ms");
	}

	private static void enableMetrics()
	{
		Metrics metrics = new Metrics(Main.getInstance(), 8246);
	}

	public static void startThreads()
	{
		new InfoCache().runTaskTimerAsynchronously(Main.getInstance(), 0, 72000);
		new ClearCache().runTaskTimerAsynchronously(Main.getInstance(), 0, 72000);
		new SaveInventories().runTaskTimerAsynchronously(Main.getInstance(), Config.SAVE_EVERY * 20, Config.SAVE_EVERY * 20);
		new SkullCache().runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
		new CleanOldLogs().runTaskTimerAsynchronously(Main.getInstance(), 0, 72000);
	}

	private static void registerListeners()
	{
		Main.getInstance().getServer().getPluginManager().registerEvents(new InventoryClick(), Main.getInstance());
		Main.getInstance().getServer().getPluginManager().registerEvents(new TabComplete(), Main.getInstance());
		Main.getInstance().getServer().getPluginManager().registerEvents(new JoinEvent(), Main.getInstance());
		Main.getInstance().getServer().getPluginManager().registerEvents(new LogListener(), Main.getInstance());
	}

	private static void registerCommands()
	{
		new InventoryLogs(Main.getInstance(), "inventorylogs", "invlogs", "inventorylog", "invlogs");
	}

	public static void banVersion()
	{
		Bukkit.getLogger().log(Level.WARNING, "§c[" + Main.getInstance().getName() + "] This version is banned, please update it.");
		Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
	}
}
