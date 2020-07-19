//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cryptomorin.xseries;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtils {
	private static final String valueProperty = "{\"textures\":{\"SKIN\":{\"url\":\"";
	private static final String textures = "https://textures.minecraft.net/texture/";
	private static final String session = "https://sessionserver.mojang.com/session/minecraft/profile/";
	private static final Pattern base64 = Pattern.compile("(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?");
	private static final Pattern username = Pattern.compile("[A-z0-9]+");

	public SkullUtils() {
	}

	@Nonnull
	public static ItemStack getSkull(@Nonnull UUID id) {
		ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
		SkullMeta meta = (SkullMeta)head.getItemMeta();
		if (XMaterial.isNewVersion()) {
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
		} else {
			meta.setOwner(id.toString());
		}

		head.setItemMeta(meta);
		return head;
	}

	@Nonnull
	public static SkullMeta applySkin(@Nonnull ItemMeta head, @Nonnull String player) {
		boolean isId = isUUID(getFullUUID(player));
		SkullMeta meta = (SkullMeta)head;
		if (isId || isUsername(player)) {
			if (isId) {
				return getSkullByValue(meta, getSkinValue(player, true));
			}

			if (XMaterial.isNewVersion()) {
				meta.setOwningPlayer(Bukkit.getOfflinePlayer(player));
			} else {
				meta.setOwner(player);
			}
		}

		if (player.contains("textures.minecraft.net")) {
			return getValueFromTextures(meta, player);
		} else {
			return player.length() > 100 && isBase64(player) ? getSkullByValue(meta, player) : getTexturesFromUrlValue(meta, player);
		}
	}

	@Nonnull
	public static SkullMeta getSkullByValue(@Nonnull SkullMeta head, @Nonnull String value) {
		Validate.notEmpty(value, "Skull value cannot be null or empty");
		GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
		profile.getProperties().put("textures", new Property("textures", value));

		try {
			Field profileField = head.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(head, profile);
		} catch (NoSuchFieldException | IllegalAccessException | SecurityException var4) {
			var4.printStackTrace();
		}

		return head;
	}

	@Nonnull
	public static SkullMeta getValueFromTextures(@Nonnull SkullMeta head, @Nonnull String url) {
		return getSkullByValue(head, encodeBase64("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}"));
	}

	@Nonnull
	public static SkullMeta getTexturesFromUrlValue(@Nonnull SkullMeta head, @Nonnull String urlValue) {
		return getValueFromTextures(head, "https://textures.minecraft.net/texture/" + urlValue);
	}

	@Nonnull
	private static String encodeBase64(@Nonnull String str) {
		return Base64.getEncoder().encodeToString(str.getBytes());
	}

	private static boolean isBase64(String base64) {
		return SkullUtils.base64.matcher(base64).matches();
	}

	@Nonnull
	public static String getSkinValue(@Nonnull ItemStack skull) {
		Objects.requireNonNull(skull, "Skull ItemStack cannot be null");
		SkullMeta meta = (SkullMeta)skull.getItemMeta();
		GameProfile profile = null;

		try {
			Field profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profile = (GameProfile)profileField.get(meta);
		} catch (NoSuchFieldException | IllegalAccessException | SecurityException var5) {
			var5.printStackTrace();
		}

		if (profile != null && !profile.getProperties().get("textures").isEmpty()) {
			Iterator var6 = profile.getProperties().get("textures").iterator();

			while(var6.hasNext()) {
				Property property = (Property)var6.next();
				if (!property.getValue().isEmpty()) {
					return property.getValue();
				}
			}
		}

		return null;
	}

	@Nullable
	private static String getFullUUID(@Nullable String id) {
		if (Strings.isNullOrEmpty(id)) {
			return id;
		} else if (id.length() == 36) {
			return id;
		} else {
			return id.length() != 32 ? id : id.substring(0, 8) + '-' + id.substring(8, 12) + '-' + id.substring(12, 16) + '-' + id.substring(16, 20) + '-' + id.substring(20, 32);
		}
	}

	private static boolean isUUID(@Nullable String id) {
		if (Strings.isNullOrEmpty(id)) {
			return false;
		} else {
			return id.length() == 36 && StringUtils.countMatches(id, "-") == 4;
		}
	}

	private static boolean isUsername(@Nullable String name) {
		if (Strings.isNullOrEmpty(name)) {
			return false;
		} else {
			return name.length() >= 3 && name.length() <= 16 && username.matcher(name).matches();
		}
	}

	@Nonnull
	public static String getSkinValue(@Nonnull String name, boolean isId) {
		Validate.notEmpty(name, "Player name/UUID cannot be null or empty");

		try {
			JsonParser parser = new JsonParser();
			String uuid;
			URL properties;
			InputStreamReader readProperties;
			JsonObject jObjectP;
			if (!isId) {
				properties = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
				readProperties = new InputStreamReader(properties.openStream());
				jObjectP = parser.parse(readProperties).getAsJsonObject();
				if (mojangError(jObjectP)) {
					return null;
				}

				uuid = jObjectP.get("id").getAsString();
			} else {
				uuid = StringUtils.remove(name, '-');
			}

			properties = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
			readProperties = new InputStreamReader(properties.openStream());
			jObjectP = parser.parse(readProperties).getAsJsonObject();
			if (mojangError(jObjectP)) {
				return null;
			} else {
				JsonObject textureProperty = jObjectP.get("properties").getAsJsonArray().get(0).getAsJsonObject();
				return textureProperty.get("value").getAsString();
			}
		} catch (IllegalStateException | IOException var8) {
			return null;
		}
	}

	private static boolean mojangError(JsonObject jsonObject) {
		if (!jsonObject.has("error")) {
			return false;
		} else {
			String err = jsonObject.get("error").getAsString();
			String msg = jsonObject.get("errorMessage").getAsString();
			System.err.println("Mojang Error " + err + ": " + msg);
			return true;
		}
	}
}