package xyz.nodrop.client.module.impl;

import xyz.nodrop.client.module.Category;
import xyz.nodrop.client.module.Module;
import xyz.nodrop.client.module.setting.BooleanSetting;
import xyz.nodrop.client.module.setting.ModeSetting;
import xyz.nodrop.client.module.setting.NumberSetting;

/** Ustawienia samego ClickGUI (nie da sie go "wlaczyc"/"wylaczyc"). */
public class GuiSettingsModule extends Module {
	public final ModeSetting accent = addSetting(new ModeSetting("Accent", "Purple", "Blue", "Green", "Red", "Pink", "Orange"));
	public final BooleanSetting dimBackground = addSetting(new BooleanSetting("Dim background", true));
	public final BooleanSetting descriptions = addSetting(new BooleanSetting("Show descriptions", true));
	public final NumberSetting animSpeed = addSetting(new NumberSetting("Animation speed", 12, 4, 30, 1));

	public GuiSettingsModule() {
		super("GUI", "Look and feel of this menu.", Category.CLIENT, false);
	}
}
