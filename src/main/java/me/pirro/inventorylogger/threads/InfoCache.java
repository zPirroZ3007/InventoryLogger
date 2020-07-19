package me.pirro.inventorylogger.threads;

import json.JSONObject;
import me.pirro.inventorylogger.Main;
import me.pirro.inventorylogger.configs.Config;
import me.pirro.inventorylogger.utils.Util;
import me.pirro.utils.WebUtils;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 29 giugno, 2020
 */
public class InfoCache extends BukkitRunnable
{
	@Override public void run()
	{

		try
		{
			Util.VERSION_NEW = WebUtils.httpsRequest("https://api.pirro.me/version_inventorylogger.txt");
			String[] bannedVersions = WebUtils.httpsRequest("https://api.pirro.me/version_banned_inventorylogger.txt").split(",");

			for (String version : bannedVersions)
				if (version.equals(Config.VERSION))
				{
					Util.banVersion();
					return;
				}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
