package me.pirro.inventorylogger.listeners;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import me.pirro.inventorylogger.objects.ItemLog;
import me.pirro.inventorylogger.utils.FileUtils;
import me.pirro.inventorylogger.utils.Lists;
import me.pirro.utils.CommandFramework;
import me.pirro.utils.DateUtils;
import me.pirro.utils.ItemStackBuilder;
import me.pirro.utils.messenger.Messenger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class InventoryClick implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR) public void onInventoryClick(InventoryClickEvent event)
	{
		if (!(event.getWhoClicked() instanceof Player))
			return;
		Player player = (Player) event.getWhoClicked();

		if (event.getView().getTitle().startsWith(FileUtils.getMessages().getString("inventory-title").replaceAll("\\{player}", "")))
		{
			if (event.getView().getTopInventory().getItem(0) != null)
				if (XMaterial.matchXMaterial(event.getView().getTopInventory().getItem(0)) == XMaterial.PLAYER_HEAD)
					event.setCancelled(true);
			if (event.getSlot() < 9)
				event.setCancelled(true);

			if (event.getCurrentItem() != null)
			{
				if (XMaterial.matchXMaterial(event.getCurrentItem()) == XMaterial.CHEST)
				{

					String logString = event.getCurrentItem().getItemMeta().getLore().get(event.getCurrentItem().getItemMeta().getLore().size() - 1).replaceAll("§0", "");

					File logFile = new File(FileUtils.getDataFolder().getAbsolutePath() + "/" + logString);

					YamlConfiguration log;
					if (!Lists.getLogsCache().containsKey(logFile.getName()))
					{
						log = YamlConfiguration.loadConfiguration(logFile);
						Lists.getLogsCache().put(logFile.getName(), log);
					}
					else
						log = Lists.getLogsCache().get(logFile.getName());

					if (event.getClick() == ClickType.LEFT)
					{
						player.closeInventory();

						Inventory inv = Bukkit.createInventory(null, 54, event.getView().getTitle());

						List<Map<?, ?>> mapList = log.getMapList("inv");
						for (Map<?, ?> logItemMap : mapList)
						{
							ItemLog itemLog = ItemLog.deserialize(logItemMap);
							inv.setItem(itemLog.getIndex() + 9, itemLog.getItem());
						}

						inv.setItem(0, new ItemStackBuilder().setItem(XMaterial.BARRIER.parseItem()).setName(ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("to-main-menu"))).build());

						player.openInventory(inv);
						return;
					}

					if (event.getClick() == ClickType.RIGHT)
					{
						int curPage = Integer.parseInt(event.getClickedInventory().getItem(6).getItemMeta().getDisplayName());
						logFile.delete();
						if (Lists.getLogsCache().containsKey(logFile.getName()))
							Lists.getLogsCache().remove(logFile.getName());
						changePage(event.getClickedInventory(), player, Lists.getLastOpened().get(player.getName()), curPage);
					}
					return;
				}

				if (XMaterial.matchXMaterial(event.getCurrentItem()) == XMaterial.BARRIER)
				{
					if (event.getSlot() > 8)
						return;
					player.closeInventory();
					String username = Lists.getLastOpened().get(player.getName());
					boolean logsFound = false;

					for (final File fileEntry : FileUtils.getDataFolder().listFiles())
						if (fileEntry.getName().startsWith(username))
							logsFound = true;

					CommandFramework.Validator.notCondition(logsFound, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("no-logs-found").replaceAll("\\{player}", username)));

					Inventory inv = Bukkit.createInventory(null, 54, FileUtils.getMessages().getString("inventory-title").replaceAll("\\{player}", username));
					ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
					SkullMeta meta = (SkullMeta) skull.getItemMeta();
					meta.setDisplayName("§e" + username);
					skull.setItemMeta(Lists.getSkullMap().get(username) == null ? meta : SkullUtils.getSkullByValue(meta, Lists.getSkullMap().get(username)));
					inv.setItem(0, skull);

					inv.setItem(6, new ItemStackBuilder().setItem(XMaterial.PAPER.parseItem()).setName("1").build());
					inv.setItem(7, new ItemStackBuilder().setItem(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("previous"))).build());
					inv.setItem(8, new ItemStackBuilder().setItem(XMaterial.LIME_STAINED_GLASS_PANE.parseItem()).setName(ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("next"))).build());

					int i = 9;

					List<YamlConfiguration> logsFinalized = new ArrayList<>();
					Map<YamlConfiguration, String> logsNameMap = new HashMap<>();

					for (final File fileEntry : FileUtils.getDataFolder().listFiles())
						if (fileEntry.getName().startsWith(username))
						{
							YamlConfiguration log;
							if (!Lists.getLogsCache().containsKey(fileEntry.getName()))
							{
								log = YamlConfiguration.loadConfiguration(fileEntry);
								Lists.getLogsCache().put(fileEntry.getName(), log);
							}
							else
								log = Lists.getLogsCache().get(fileEntry.getName());
							logsNameMap.put(log, fileEntry.getName());
							logsFinalized.add(log);
						}
					logs(inv, i, logsFinalized, logsNameMap);

					Lists.getLastOpened().put(player.getName(), username);

					player.openInventory(inv);
				}

				if (XMaterial.matchXMaterial(event.getCurrentItem()) == XMaterial.RED_STAINED_GLASS_PANE)
				{
					if (event.getSlot() > 8)
						return;
					int curPage = Integer.parseInt(event.getClickedInventory().getItem(6).getItemMeta().getDisplayName());
					if (curPage > 1)
					{
						curPage--;

						changePage(event.getClickedInventory(), player, Lists.getLastOpened().get(player.getName()), curPage);
					}
					return;
				}

				if (XMaterial.matchXMaterial(event.getCurrentItem()) == XMaterial.LIME_STAINED_GLASS_PANE)
				{
					if (event.getSlot() > 8)
						return;
					int curPage = Integer.parseInt(event.getClickedInventory().getItem(6).getItemMeta().getDisplayName());
					curPage++;

					changePage(event.getClickedInventory(), player, Lists.getLastOpened().get(player.getName()), curPage);
				}
			}
		}
	}

	private void changePage(Inventory inv, Player player, String username, int page)
	{
		int i = 9;
		List<YamlConfiguration> logsFinalized = new ArrayList<>();
		Map<YamlConfiguration, String> logsNameMap = new HashMap<>();

		for (final File fileEntry : FileUtils.getDataFolder().listFiles())
			if (fileEntry.getName().startsWith(username))
			{
				YamlConfiguration log;
				if (!Lists.getLogsCache().containsKey(fileEntry.getName()))
				{
					log = YamlConfiguration.loadConfiguration(fileEntry);
					Lists.getLogsCache().put(fileEntry.getName(), log);
				}
				else
					log = Lists.getLogsCache().get(fileEntry.getName());
				logsNameMap.put(log, fileEntry.getName());
				logsFinalized.add(log);
			}
		logsFinalized.sort((o1, o2) -> Long.compare(o2.getLong("date"), o1.getLong("date")));
		for (int y = 0; y < (page * 45) - 45; y++)
		{
			if (logsFinalized.isEmpty())
				break;
			logsFinalized.remove(0);
		}

		if (logsFinalized.isEmpty())
		{
			player.closeInventory();
			Messenger.sendErrorMessage(player, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("empty-page")));
			return;
		}

		for (int x = 9; x < 54; x++)
			inv.setItem(x, XMaterial.AIR.parseItem());

		logs(inv, i, logsFinalized, logsNameMap);

		inv.setItem(6, new ItemStackBuilder().setItem(XMaterial.PAPER.parseMaterial().orElse(Material.STONE), Math.min(page, 64)).setName(page + "").build());

		player.updateInventory();
	}

	public static void logs(Inventory inv, int i, List<YamlConfiguration> logsFinalized, Map<YamlConfiguration, String> logsNameMap) {


		for (YamlConfiguration log : logsFinalized)
		{
			if (i == 54)
				break;
			List<String> lore = new ArrayList<>();
			for (String a : FileUtils.getMessages().getStringList("log-lore"))
				lore.add(ChatColor.translateAlternateColorCodes('&', a));
			lore.add("§0" + logsNameMap.get(log));
			inv.setItem(i, new ItemStackBuilder().setItem(XMaterial.CHEST.parseItem()).setName("§a" + DateUtils.millisToDate(log.getLong("date"))).setLore(lore).build());
			i++;
		}
	}

}
