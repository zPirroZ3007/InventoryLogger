package me.pirro.inventorylogger.commands;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import me.pirro.inventorylogger.Main;
import me.pirro.inventorylogger.utils.FileUtils;
import me.pirro.inventorylogger.utils.Lists;
import me.pirro.inventorylogger.utils.Util;
import me.pirro.utils.CommandFramework;
import me.pirro.utils.DateUtils;
import me.pirro.utils.ItemStackBuilder;
import me.pirro.utils.TextComponentBuilder;
import me.pirro.utils.messenger.Messenger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class InventoryLogs extends CommandFramework
{
	public InventoryLogs(JavaPlugin plugin, String label, String... aliases)
	{
		super(plugin, label, aliases);
	}

	public void execute(CommandSender sender, String label, String[] args)
	{
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("reload"))
			{
				Validator.notCondition(sender.hasPermission("inventorylogs.admin"), ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("no-permissions")));
				if (args.length == 1)
				{
					FileUtils.ReloadConfigs();
					Messenger.sendSuccessMessage(sender, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("reload")));
					return;
				}
				helpMessage(sender, label);
				return;
			}

			if (args[0].equalsIgnoreCase("clear"))
			{
				Validator.notCondition(sender.hasPermission("inventorylogs.admin"), ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("no-permissions")));

				if (args.length == 1)
				{
					for (final File fileEntry : FileUtils.getDataFolder().listFiles())
						fileEntry.delete();

					Messenger.sendSuccessMessage(sender, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("every-log-removed")));
					return;
				}

				if (args.length == 2)
				{
					if (args[1].equalsIgnoreCase("old"))
					{
						Messenger.sendSuccessMessage(sender, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("removing-logs")));
						new BukkitRunnable()
						{
							@Override public void run()
							{
								for (final File fileEntry : FileUtils.getDataFolder().listFiles())
								{
									YamlConfiguration log = YamlConfiguration.loadConfiguration(fileEntry);

									if (System.currentTimeMillis() - log.getLong("date") > 259200000L)
										fileEntry.delete();
								}
								Messenger.sendSuccessMessage(sender, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("every-log-removed")));
							}
						}.runTaskAsynchronously(Main.getInstance());
						return;
					}

					for (final File fileEntry : FileUtils.getDataFolder().listFiles())
						if (fileEntry.getName().startsWith(args[1]))
							fileEntry.delete();

					Messenger.sendSuccessMessage(sender, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("logs-removed-for").replaceAll("\\{player}", args[1])));
					return;
				}

				helpMessage(sender, label);
				return;
			}

			if (args[0].equalsIgnoreCase("about"))
			{
				sender.sendMessage("");
				sender.spigot().sendMessage(new TextComponent(" §6" + Main.getInstance().getName() + " §6v" + Main.getInstance().getDescription().getVersion() + " §eby §6zPirroZ3007\n" + " §ePurchase it on "), new TextComponentBuilder().setText("§nhttps://inventorylogger.pirro.me/").setColor(ChatColor.AQUA).setHover("§7Click here to purchase the plugin").build());
				sender.sendMessage("");
				return;
			}

			Validator.notCondition(sender.hasPermission("inventorylogs.logs"), ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("no-permissions")));

			Validator.notCondition(sender instanceof Player, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("must-be-player")));
			Player player = (Player) sender;
			String username = args[0];
			boolean logsFound = false;

			for (final File fileEntry : FileUtils.getDataFolder().listFiles())
				if (fileEntry.getName().startsWith(username))
					logsFound = true;

			Validator.notCondition(logsFound, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("no-logs-found").replaceAll("\\{player}", username)));

			Messenger.sendMessage(player, ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("loading")));

			new BukkitRunnable()
			{
				@Override public void run()
				{
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
					logsFinalized.sort((o1, o2) -> o1.getLong("date") > o2.getLong("date") ? -1 : 1);

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

					Lists.getLastOpened().put(player.getName(), username);
					new BukkitRunnable()
					{
						@Override public void run()
						{
							player.openInventory(inv);
						}
					}.runTask(Main.getInstance());
				}
			}.runTaskAsynchronously(Main.getInstance());
			return;
		}

		Validator.notCondition(sender.hasPermission("inventorylogs.logs"), ChatColor.translateAlternateColorCodes('&', FileUtils.getMessages().getString("no-permissions")));
		helpMessage(sender, label);
	}

	private void helpMessage(CommandSender sender, String label)
	{
		StringBuilder sb = new StringBuilder();
		for (String x : FileUtils.getMessages().getStringList("help"))
			if (x.equals(""))
				sb.append(" \n");
			else
				sb.append(ChatColor.translateAlternateColorCodes('&', x.replaceAll("\\{player}", sender.getName()).replaceAll("\\{label}", label))).append("\n");
		sender.sendMessage(sb.toString());
	}
}
