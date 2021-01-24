package me.pirro.inventorylogger.objects;

import me.pirro.utils.XMaterial;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
@Getter public class ItemLog
{
	private ItemStack item;
	private int index;

	public ItemLog(int index, ItemStack item)
	{
		this.index = index;
		this.item = item;
	}

	public Map<?, ?> serialize()
	{
		Map<Object, Object> serializeMap = new HashMap<>();
		serializeMap.put("index", getIndex());
		serializeMap.put("item", getItem() == null ? null : getItem().serialize());

		return serializeMap;
	}

	public static ItemLog deserialize(Map<? ,?> map)
	{
		return new ItemLog(Integer.parseInt(String.valueOf(map.get("index"))), map.get("item") == null ? XMaterial.AIR.parseItem() : ItemStack.deserialize((Map<String, Object>) map.get("item")));
	}
}
