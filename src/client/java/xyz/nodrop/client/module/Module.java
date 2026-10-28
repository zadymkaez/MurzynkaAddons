package xyz.nodrop.client.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
	private final String name;
	private final String description;
	private final Category category;
	private final boolean toggleable;
	private final List<Setting<?>> settings = new ArrayList<>();
	private boolean enabled;

	protected Module(String name, String description, Category category) {
		this(name, description, category, true);
	}

	protected Module(String name, String description, Category category, boolean toggleable) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.toggleable = toggleable;
	}

	protected <S extends Setting<?>> S addSetting(S setting) {
		settings.add(setting);
		return setting;
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	public void setEnabled(boolean value) {
		if (!toggleable || enabled == value) return;
		enabled = value;
		if (value) onEnable(); else onDisable();
	}

	public String getName() { return name; }
	public String getDescription() { return description; }
	public Category getCategory() { return category; }
	public boolean isToggleable() { return toggleable; }
	public boolean isEnabled() { return enabled; }
	public List<Setting<?>> getSettings() { return settings; }

	/** Hooki - nadpisuj w modulach. */
	public void onEnable() {}
	public void onDisable() {}
	public void onTick(Minecraft client) {}
	public void onHud(GuiGraphicsExtractor graphics, Minecraft client) {}
}
