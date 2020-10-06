package me.pirro.inventorylogger;

import lombok.Getter;
import me.pirro.inventorylogger.utils.Util;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class Main extends JavaPlugin implements Listener
{
	@Getter private static Main instance;

	@Override public void onEnable()
	{
		instance = this;
		Util.enable();
	}

	@Override public void onDisable()
	{
		Util.disable();
	}
}