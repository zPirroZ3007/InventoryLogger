package me.pirro.inventorylogger.listeners;

import me.pirro.inventorylogger.configs.Config;
import me.pirro.inventorylogger.objects.ItemLog;
import me.pirro.inventorylogger.utils.FileUtils;
import me.pirro.inventorylogger.utils.Lists;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 05 luglio, 2020
 */
public class LogListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR) public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (Config.LOG_ON_JOIN)
			log(player);
	}

	@EventHandler(priority = EventPriority.MONITOR) public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if (Config.LOG_ON_QUIT)
			log(player);
	}

	@EventHandler(priority = EventPriority.MONITOR) public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		if (Config.LOG_ON_DEATH)
			log(player);
	}

	@EventHandler(priority = EventPriority.MONITOR) public void onPlayerChangeWorld(PlayerChangedWorldEvent event)
	{
		Player player = event.getPlayer();
		if (Config.LOG_ON_WORLD_CHANGE)
			log(player);
	}

	private void log(Player player)
	{
		if (!Config.LOG_EMPTY_INVENTORIES)
		{
			boolean empty = true;
			for (ItemStack item : player.getInventory().getContents())
				if (item != null)
				{
					empty = false;
					break;
				}
			if (empty)
				return;
		}

		List<Map<?, ?>> items = new ArrayList<>();
		ItemStack[] contents = player.getInventory().getContents();
		for (int x = 0; x < contents.length; x++)
			items.add(new ItemLog(x, contents[x]).serialize());

		int logI = 0;

		while (new File(FileUtils.getDataFolder() + "/" + player.getName() + "-" + logI + ".yml").exists())
			logI++;

		try
		{
			File logFile = new File(FileUtils.getDataFolder() + "/" + player.getName() + "-" + logI + ".yml");
			logFile.createNewFile();

			YamlConfiguration log = YamlConfiguration.loadConfiguration(logFile);
			log.set("date", System.currentTimeMillis());
			log.set("inv", items);
			log.save(logFile);
			Lists.getLogsCache().put(logFile.getName(), log);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
