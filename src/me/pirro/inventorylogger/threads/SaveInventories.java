package me.pirro.inventorylogger.threads;

import me.pirro.inventorylogger.Main;
import me.pirro.inventorylogger.configs.Config;
import me.pirro.inventorylogger.objects.ItemLog;
import me.pirro.inventorylogger.utils.FileUtils;
import me.pirro.inventorylogger.utils.Lists;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class SaveInventories extends BukkitRunnable
{
	@Override public void run()
	{
		if (!Config.LOG_ENABLED)
			return;

		long start = System.currentTimeMillis();
		for (Player player : Bukkit.getOnlinePlayers())
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
					continue;
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

		if (Config.DEBUG)
			Bukkit.getLogger().log(Level.INFO, "§e[" + Main.getInstance().getName() + "] [DEBUG] Inventories logged in " + (System.currentTimeMillis() - start) + "ms");
	}
}
