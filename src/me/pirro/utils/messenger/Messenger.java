package me.pirro.utils.messenger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Messenger
{
	public static void sendSuccessMessage(Player player, String message)
	{
		player.sendMessage("§a" + message);
	}

	public static void sendMessage(Player player, String message)
	{
		player.sendMessage("§7" + message);
	}

	public static void sendErrorMessage(Player player, String message)
	{
		player.sendMessage("§c" + message);
	}

	public static void sendWarnMessage(Player player, String message)
	{
		player.sendMessage("§e" + message);
	}

	public static void sendSuccessMessage(CommandSender player, String message)
	{
		player.sendMessage("§a" + message);
	}

	public static void sendMessage(CommandSender player, String message)
	{
		player.sendMessage("§7" + message);
	}

	public static void sendErrorMessage(CommandSender player, String message)
	{
		player.sendMessage("§c" + message);
	}

	public static void sendWarnMessage(CommandSender player, String message)
	{
		player.sendMessage("§e" + message);
	}

	public static void sendMessage(CommandSender sender, BaseComponent[] message)
	{
		sender.spigot().sendMessage(message);
	}

	public static void sendMessage(Player player, BaseComponent[] message)
	{
		player.spigot().sendMessage(message);
	}

	public static void sendSuccessMessage(Player player, TextComponent message)
	{
		message.setColor(ChatColor.GREEN);
		player.spigot().sendMessage(message);
	}

	public static void sendMessage(Player player, TextComponent message)
	{
		message.setColor(ChatColor.GRAY);
		player.spigot().sendMessage(message);
	}

	public static void sendErrorMessage(Player player, TextComponent message)
	{
		message.setColor(ChatColor.RED);
		player.spigot().sendMessage(message);
	}

	public static void sendWarnMessage(Player player, TextComponent message)
	{
		message.setColor(ChatColor.YELLOW);
		player.spigot().sendMessage(message);
	}

	public static void sendSuccessMessage(CommandSender player, TextComponent message)
	{
		message.setColor(ChatColor.GREEN);
		player.spigot().sendMessage(message);
	}

	public static void sendMessage(CommandSender player, TextComponent message)
	{
		message.setColor(ChatColor.GRAY);
		player.spigot().sendMessage(message);
	}

	public static void sendErrorMessage(CommandSender player, TextComponent message)
	{
		message.setColor(ChatColor.RED);
		player.spigot().sendMessage(message);
	}

	public static void sendWarnMessage(CommandSender player, TextComponent message)
	{
		message.setColor(ChatColor.YELLOW);
		player.spigot().sendMessage(message);
	}

	public static void sendJsonMessage(CommandSender sender, String json)
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + sender.getName() + " " + json);
	}

	public static void sendJsonMessage(Player player, String json)
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + player.getName() + " " + json);
	}

	public static void sendSuccessActionBarMessage(Player player, String message)
	{
		TextComponent t = new TextComponent(message);
		t.setColor(ChatColor.GREEN);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, t);
	}

	public static void sendWarnActionBarMessage(Player player, String message)
	{
		TextComponent t = new TextComponent(message);
		t.setColor(ChatColor.YELLOW);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, t);
	}

	public static void sendErrorActionBarMessage(Player player, String message)
	{
		TextComponent t = new TextComponent(message);
		t.setColor(ChatColor.RED);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, t);
	}
}