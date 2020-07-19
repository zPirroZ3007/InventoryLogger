package me.pirro.inventorylogger.threads;

import me.pirro.inventorylogger.utils.Lists;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 30 giugno, 2020
 */
public class ClearCache extends BukkitRunnable
{
	@Override public void run()
	{
		Lists.getLogsCache().clear();
	}
}
