package me.pirro.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 29 giugno, 2020
 */
public class WebUtils
{
	public static String httpRequest(String link)
	{
		try
		{
			URL url = new URL(link);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			return getString(con);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	private static String getString(HttpURLConnection con) throws IOException
	{
		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	public static String httpsRequest(String link)
	{
		try
		{
			URL url = new URL(link);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			return getString(con);
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}
