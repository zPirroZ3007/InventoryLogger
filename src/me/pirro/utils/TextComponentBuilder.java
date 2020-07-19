package me.pirro.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Created by @zPirroZ3007 (github.com/zPirroZ3007) on 26 dicembre, 2019
 */
public class TextComponentBuilder
{
	private TextComponent component;
	public TextComponentBuilder()
	{
	}

	public TextComponentBuilder setText(String text)
	{
		component = new TextComponent(text);
		return this;
	}

	public TextComponentBuilder setColor(ChatColor color)
	{
		component.setColor(color);
		return this;
	}

	public TextComponentBuilder setHover(String text)
	{
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(text).create()));
		return this;
	}

	public TextComponentBuilder setUrl(String url)
	{
		component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		return this;
	}

	public TextComponentBuilder setCommand(String command)
	{
		component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		return this;
	}

	public TextComponentBuilder setSuggestCommand(String command)
	{
		component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
		return this;
	}

	public TextComponent build()
	{
		return component;
	}
}
