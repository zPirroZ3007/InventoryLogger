package me.pirro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
	public static long parseDuration(String duration)
	{
		int dur = Integer.parseInt(duration.toLowerCase().replaceAll("a", "").replaceAll("b", "").replaceAll("c", "").replaceAll("d", "").replaceAll("e", "").replaceAll("f", "").replaceAll("g", "").replaceAll("h", "").replaceAll("i", "").replaceAll("j", "").replaceAll("k", "").replaceAll("l", "").replaceAll("m", "").replaceAll("n", "").replaceAll("o", "").replaceAll("p", "").replaceAll("q", "").replaceAll("r", "").replaceAll("s", "").replaceAll("t", "").replaceAll("u", "").replaceAll("v", "").replaceAll("w", "").replaceAll("x", "").replaceAll("y", "").replaceAll("z", ""));
		String unit = duration.toLowerCase().replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "");
		return (unit.equalsIgnoreCase("s") ? dur * 1000 : unit.equalsIgnoreCase("m") ? dur * 1000 * 60 : unit.equalsIgnoreCase("h") ? dur * 1000 * 60 * 60 : unit.equalsIgnoreCase("d") ? dur * 1000 * 60 * 60 * 24 : unit.equalsIgnoreCase("w") ? dur * 1000 * 60 * 60 * 24 * 7 : dur * 1000);
	}

	public static String millisToDate(long millis)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date(millis);

		return sdf.format(date);
	}

	public static String millisToDateNoHour(long millis)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date(millis);

		return sdf.format(date);
	}

	public static String millisToHour(long millis)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(millis);

		return sdf.format(date);
	}

	public static long dateToMillis(String date)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date d = sdf.parse(date);

			return d.getTime();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return 0;
	}

	public static String getDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date(System.currentTimeMillis());

		return sdf.format(date);
	}

	public static String formatDuration(long endSec, long startSec)
	{
		String output = "";
		long durationTmp;

		long durationSec = (endSec - startSec) / 1000;
		if (durationSec < 60)
		{
			output += durationSec + (durationSec == 1 ? " secondo" : " secondi");
		}
		else
		{
			boolean space = false;
			if(durationSec >= 31536000)
			{
				durationTmp = durationSec / 31536000;
				durationSec -= durationTmp * 31536000;
				output += durationTmp + (durationTmp == 1 ? " anno" : " anni");
				space = true;
			}
			if(durationSec >= 2592000)
			{
				durationTmp = durationSec / 2592000;
				durationSec -= durationTmp * 2592000;
				if (space)
					output += " ";
				output += durationTmp + (durationTmp == 1 ? " mese" : " mesi");
				space = true;
			}
			if (durationSec >= 604800)
			{
				durationTmp = durationSec / 604800;
				durationSec -= durationTmp * 604800;
				if (space)
					output += " ";
				output += durationTmp + (durationTmp == 1 ? " settimana" : " settimane");
				space = true;
			}
			if (durationSec >= 86400)
			{
				durationTmp = durationSec / 86400;
				durationSec -= durationTmp * 86400;
				if (space)
					output += " ";
				output += durationTmp + (durationTmp == 1 ? " giorno" : " giorni");
				space = true;
			}
			if (durationSec >= 3600)
			{
				durationTmp = durationSec / 3600;
				durationSec -= durationTmp * 3600;
				if (space)
					output += " ";
				output += durationTmp + (durationTmp == 1 ? " ora" : " ore");
				space = true;
			}
			if (durationSec >= 60)
			{
				durationTmp = durationSec / 60;
				durationSec -= durationTmp * 60;
				if (space)
					output += " ";
				output += durationTmp + (durationTmp == 1 ? " minuto" : " minuti");
				space = true;
			}
			if (durationSec > 0)
			{
				if (space)
					output += " ";
				output += durationSec + (durationSec == 1 ? " secondo" : " secondi");
			}
		}

		return output;
	}
}
