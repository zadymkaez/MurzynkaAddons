package xyz.nodrop.client.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.module.impl.GuiSettingsModule;
import xyz.nodrop.client.module.impl.ModuleListModule;
import xyz.nodrop.client.module.impl.SprintModule;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
	private static final List<Module> MODULES = new ArrayList<>();

	private ModuleManager() {}

	public static void init() {
		register(new SprintModule());
		register(new ModuleListModule());
		register(new GuiSettingsModule());
	}

	private static void register(Module module) {
		MODULES.add(module);
	}

	public static List<Module> getModules() {
		return MODULES;
	}

	public static List<Module> getByCategory(Category category) {
		List<Module> result = new ArrayList<>();
		for (Module m : MODULES) {
			if (m.getCategory() == category) result.add(m);
		}
		return result;
	}

	public static <T extends Module> T get(Class<T> type) {
		for (Module m : MODULES) {
			if (type.isInstance(m)) return type.cast(m);
		}
		return null;
	}

	public static void onTick(Minecraft client) {
		if (client.player == null) return;
		for (Module m : MODULES) {
			if (m.isEnabled()) m.onTick(client);
		}
	}

	public static void onHud(GuiGraphicsExtractor graphics) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.gui.hud.isHidden()) return;
		for (Module m : MODULES) {
			if (m.isEnabled()) m.onHud(graphics, mc);
		}
	}
}
