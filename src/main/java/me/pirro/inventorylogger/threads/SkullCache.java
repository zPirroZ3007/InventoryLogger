package me.pirro.inventorylogger.threads;

import me.pirro.inventorylogger.utils.Lists;
import me.pirro.utils.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class SkullCache extends BukkitRunnable
{
	@Override public void run()
	{
		for (Player player : Bukkit.getOnlinePlayers())
			if (!Lists.getSkullMap().containsKey(player.getName()))
				try
				{
					Lists.getSkullMap().put(player.getName(), SkullUtils.getSkinValue(player.getName(), false));
				}
				catch (Exception ex)
				{

				}
	}
}