package me.pirro.inventorylogger.utils;

import lombok.Getter;
import me.pirro.inventorylogger.objects.Log;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 28 giugno, 2020
 */
public class Lists
{
	@Getter private static Map<String, String> skullMap = new HashMap<>();
	@Getter private static Map<String, YamlConfiguration> logsCache = new LinkedHashMap<>();
	@Getter private static Map<String, String> lastOpened = new HashMap<>();

}
