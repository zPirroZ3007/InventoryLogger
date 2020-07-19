package me.pirro.utils;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 26 dicembre, 2019
 */
public class ItemStackBuilder
{
	private Material material;
	private List<String> lore = new ArrayList<>();
	private String name;
	private int quantity;
	private String skullName;
	private ItemStack item;

	public ItemStackBuilder()
	{
	}

	public ItemStackBuilder setItem(Material material, int quantity)
	{
		this.material = material;
		this.quantity = quantity;
		return this;
	}

	public ItemStackBuilder setLore(String... strings)
	{
		lore.addAll(Arrays.asList(strings));
		return this;
	}

	public ItemStackBuilder setLore(List<String> strings)
	{
		lore.addAll(strings);
		return this;
	}

	public ItemStackBuilder setName(String name)
	{
		this.name = name;
		return this;
	}

	public ItemStackBuilder skullName(String name)
	{
		this.skullName = name;
		return this;
	}

	public ItemStackBuilder setItem(ItemStack item)
	{
		this.item = item;
		return this;
	}

	public ItemStack build()
	{
		ItemStack itemStack = item == null ? new ItemStack(material, quantity) : item;
		ItemStack finalItem;
		ItemMeta defaultMeta = itemStack.getItemMeta();
		if (material == XMaterial.PLAYER_HEAD.parseMaterial())
		{

			finalItem = itemStack.clone();;
			SkullMeta itemMeta = (SkullMeta) finalItem.getItemMeta();
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			itemMeta.setLore(lore);
			itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.nameUUIDFromBytes(("OfflinePlayer:" + skullName).getBytes(Charsets.UTF_8))));
			finalItem.setItemMeta(itemMeta);
		}
		else
		{
			finalItem = itemStack.clone();
			ItemMeta itemMeta = finalItem.getItemMeta();
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			itemMeta.setLore(lore);
			finalItem.setItemMeta(itemMeta);
		}
		try
		{
			return finalItem;
		}
		finally
		{
			itemStack.setItemMeta(defaultMeta);
		}
	}
}
