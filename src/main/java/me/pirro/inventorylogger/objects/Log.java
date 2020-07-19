package me.pirro.inventorylogger.objects;

import lombok.Getter;

import java.util.*;


/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 30 giugno, 2020
 */
@Getter
public class Log
{
	private long date;
	private List<ItemLog> logItems;

	public Log(long date, List<ItemLog> logItems)
	{
		this.date = date;
		this.logItems = logItems;
	}
}
