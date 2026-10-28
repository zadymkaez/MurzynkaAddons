package xyz.nodrop.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import xyz.nodrop.client.MurzynkaAddonsClient;
import xyz.nodrop.client.gui.PanelLayout;
import xyz.nodrop.client.module.Category;
import xyz.nodrop.client.module.Module;
import xyz.nodrop.client.module.ModuleManager;
import xyz.nodrop.client.module.setting.Setting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private ConfigManager() {}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve(MurzynkaAddonsClient.MOD_ID + ".json");
	}

	public static void save() {
		JsonObject root = new JsonObject();

		JsonObject modules = new JsonObject();
		for (Module m : ModuleManager.getModules()) {
			JsonObject o = new JsonObject();
			if (m.isToggleable()) o.addProperty("enabled", m.isEnabled());
			JsonObject settings = new JsonObject();
			for (Setting<?> s : m.getSettings()) settings.add(s.getName(), s.toJson());
			o.add("settings", settings);
			modules.add(m.getName(), o);
		}
		root.add("modules", modules);

		JsonObject panels = new JsonObject();
		for (Category c : Category.values()) {
			float[] st = PanelLayout.get(c);
			JsonObject p = new JsonObject();
			p.addProperty("x", st[0]);
			p.addProperty("y", st[1]);
			p.addProperty("open", st[2] >= 0.5f);
			panels.add(c.name(), p);
		}
		root.add("panels", panels);

		try {
			Files.writeString(file(), GSON.toJson(root));
		} catch (IOException e) {
			System.err.println("[" + MurzynkaAddonsClient.MOD_ID + "] Could not save config: " + e);
		}
	}

	public static void load() {
		Path path = file();
		if (!Files.exists(path)) return;
		try {
			JsonObject root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();

			JsonObject modules = root.has("modules") ? root.getAsJsonObject("modules") : new JsonObject();
			for (Module m : ModuleManager.getModules()) {
				if (!modules.has(m.getName())) continue;
				JsonObject o = modules.getAsJsonObject(m.getName());
				if (o.has("settings")) {
					JsonObject settings = o.getAsJsonObject("settings");
					for (Setting<?> s : m.getSettings()) {
						JsonElement el = settings.get(s.getName());
						if (el == null) continue;
						try { s.fromJson(el); } catch (RuntimeException ignored) {}
					}
				}
				if (m.isToggleable() && o.has("enabled")) m.setEnabled(o.get("enabled").getAsBoolean());
			}

			if (root.has("panels")) {
				JsonObject panels = root.getAsJsonObject("panels");
				for (Category c : Category.values()) {
					if (!panels.has(c.name())) continue;
					JsonObject p = panels.getAsJsonObject(c.name());
					float[] st = PanelLayout.get(c);
					st[0] = p.get("x").getAsFloat();
					st[1] = p.get("y").getAsFloat();
					st[2] = p.get("open").getAsBoolean() ? 1f : 0f;
				}
			}
		} catch (Exception e) {
			System.err.println("[" + MurzynkaAddonsClient.MOD_ID + "] Could not load config: " + e);
		}
	}
}
