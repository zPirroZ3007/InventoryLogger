package me.pirro.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.pirro.utils.ReflectionUtils;

/**
 * Wrapper for the default command executor.
 */
public abstract class CommandFramework implements CommandExecutor {

	protected JavaPlugin plugin;
	protected String label;

	public CommandFramework(JavaPlugin plugin, String label) {
		this(plugin, label, new String[0]);
	}

	public CommandFramework(JavaPlugin plugin, String label, String... aliases) {
		this.plugin = plugin;
		this.label = label;

		PluginCommand pluginCommand = plugin.getCommand(label);

		if (pluginCommand == null) {
			try {
				CommandMap commandMap = (CommandMap) ReflectionUtils.getPrivateField(Bukkit.getPluginManager(), "commandMap");
				Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
				commandConstructor.setAccessible(true);
				pluginCommand = commandConstructor.newInstance(label, plugin);
				if (aliases != null && aliases.length > 0) {
					for (String alias : aliases) {
						if (alias == null || alias.isEmpty()) {
							throw new RuntimeException("Empty or null alias");
						}
						if (alias.contains(":")) {
							throw new RuntimeException("Illegal characters in alias");
						}
					}

					pluginCommand.setAliases(Arrays.asList(aliases));
				}

				if (!commandMap.register(plugin.getName(), pluginCommand)) {
					throw new RuntimeException("Could not overwrite existing command");
				}

			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Impossibile registrare al volo il comando \"" + label + "\"");
				return;
			}
		}

		pluginCommand.setExecutor(this);

		Permission permission = getClass().getAnnotation(Permission.class);
		if (permission != null) {
			pluginCommand.setPermission(permission.value());
		}

		NoPermissionMessage noPermMsg = getClass().getAnnotation(NoPermissionMessage.class);
		if (noPermMsg != null) {
			pluginCommand.setPermissionMessage(noPermMsg.value());
		} else {
			pluginCommand.setPermissionMessage(ChatColor.RED + "Non hai accesso a quel comando.");
		}
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			execute(sender, label, args);

		} catch (ExecuteException ex) {

			if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
				// Usa il rosso di default
				sender.sendMessage(ChatColor.RED + ex.getMessage());
			}
		}
		return true;
	}

	public abstract void execute(CommandSender sender, String label, String[] args);

	public static class ExecuteException extends RuntimeException {

		private static final long serialVersionUID = 7052164163215272979L;

		@Override
		public String getMessage() {
			return super.getMessage();
		}

		public ExecuteException(String msg) {
			super(msg);
		}

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface Permission {

		String value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface NoPermissionMessage {

		String value();

	}

	public static class Validator {

		public static void notNull(Object o, String msg) {
			if (o == null) {
				throw new ExecuteException(msg);
			}
		}

		public static void Condition(boolean condition, String errorMessage) {
			if (condition) {
				throw new ExecuteException(errorMessage);
			}
		}

		public static void notCondition(boolean condition, String errorMessage) {
			if (!condition) {
				throw new ExecuteException(errorMessage);
			}
		}

		public static void ConditionJson(boolean condition, String json, Player player) {
			if (condition) {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tellraw " + player.getName() + " " + json);
				throw new ExecuteException(null);
			}
		}

		public static Player getPlayerSender(CommandSender sender) {
			if (sender instanceof Player) {
				return (Player) sender;
			} else {
				throw new ExecuteException("Questo comando non è eseguibile da CONSOLE.");
			}
		}

		public static double getDouble(String input) {
			try {
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				throw new ExecuteException("Il numero inserito non è valido.");
			}
		}

		public static int getInteger(String input) {
			try {
				int i = Integer.parseInt(input);
				return i;
			} catch (NumberFormatException e) {
				throw new ExecuteException("Il numero inserito non è valido.");
			}
		}

		public static int getPositiveInteger(String input) {
			try {
				int i = Integer.parseInt(input);
				if (i < 0) {
					throw new ExecuteException("Devi inserire un numero positivo.");
				}
				return i;
			} catch (NumberFormatException e) {
				throw new ExecuteException("Il numero inserito non è valido.");
			}
		}

		public static int getPositiveIntegerNotZero(String input) {
			try {
				int i = Integer.parseInt(input);
				if (i <= 0) {
					throw new ExecuteException("Devi inserire un numero positivo.");
				}
				return i;
			} catch (NumberFormatException e) {
				throw new ExecuteException("Il numero inserito non è valido.");
			}
		}

		public static void minLength(Object[] array, int minLength, String msg) {
			if (array.length < minLength) {
				throw new ExecuteException(msg);
			}
		}

		public static void Permission(Player player, String permission) {
			if(!player.hasPermission(permission)){
				throw new ExecuteException("Non hai accesso a quel comando!");
			}
		}

		public static void isDouble(String s, String campo) {
			try {
				Double.parseDouble(s);
			} catch (NumberFormatException e) {
				throw new ExecuteException("Il campo " + campo + " deve essere un valore numerico valido.");
			}
		}

		public static void isInteger(String s, String campo) {
			try {
				Integer.parseInt(s);
			} catch (NumberFormatException e) {
				throw new ExecuteException("Il campo " + campo + " deve essere un valore numerico valido.");
			}
		}
	}
}