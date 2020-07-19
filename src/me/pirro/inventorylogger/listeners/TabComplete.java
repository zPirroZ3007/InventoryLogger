package me.pirro.inventorylogger.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class TabComplete implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR) public void onTabComplete(TabCompleteEvent event)
	{
		String[] a = event.getBuffer().split(" ");
		if (a.length > 2)
			return;

		if (event.getBuffer().toLowerCase().startsWith("/inventorylogs ")
				|| event.getBuffer().toLowerCase().startsWith("/inventorylog ")
				|| event.getBuffer().toLowerCase().startsWith("/invlogs ")
				|| event.getBuffer().toLowerCase().startsWith("/invlog "))
		{
			List<String> completions = new ArrayList<>();
			completions.add("about");
			completions.add("reload");
			completions.add("clear");
			for(Player player : Bukkit.getOnlinePlayers())
				completions.add(player.getName());
			event.setCompletions(getCompletions(event.getBuffer(), completions));
		}
	}

	private List<String> getCompletions(String label, List<String> completions)
	{
		List<String> suggest = new ArrayList<>();

		for (String query : completions)
		{
			String[] x = label.toLowerCase().split(" ");

			if (x.length == 1)
			{
				suggest.addAll(completions);
				break;
			}
			if (x.length > 2)
				return suggest;

			if (query.toLowerCase().equals(x[1].toLowerCase()))
				return suggest;

			if (x[1].equals(""))
				suggest.addAll(completions);

			if (query.toLowerCase().startsWith(x[1].toLowerCase()))
				suggest.add(query);
		}
		return suggest;
	}
}
