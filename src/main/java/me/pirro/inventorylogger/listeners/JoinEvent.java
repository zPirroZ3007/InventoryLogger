package me.pirro.inventorylogger.listeners;

import me.pirro.inventorylogger.configs.Config;
import me.pirro.inventorylogger.utils.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 29 giugno, 2020
 */
public class JoinEvent implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR) public void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(!Config.VERSION.equals(Util.VERSION_NEW))
			if(player.hasPermission("inventorylogs.admin"))
				player.sendMessage(" \n §6InventoryLogger v" + Util.VERSION_NEW + " §eis available!\n §eDownload it on §b§nhttps://inventorylogger.pirro.me/§r\n ");
	}
}
