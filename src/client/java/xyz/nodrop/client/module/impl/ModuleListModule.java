package xyz.nodrop.client.module.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.gui.RenderUtil;
import xyz.nodrop.client.gui.Theme;
import xyz.nodrop.client.module.Category;
import xyz.nodrop.client.module.Module;
import xyz.nodrop.client.module.ModuleManager;
import xyz.nodrop.client.module.setting.BooleanSetting;
import xyz.nodrop.client.module.setting.ModeSetting;

import java.util.Comparator;
import java.util.List;

public class ModuleListModule extends Module {
	private final ModeSetting position = addSetting(new ModeSetting("Position", "Right", "Left"));
	private final BooleanSetting background = addSetting(new BooleanSetting("Background", true));
	private final BooleanSetting bar = addSetting(new BooleanSetting("Accent bar", true));

	public ModuleListModule() {
		super("Module List", "Lists all enabled modules on the HUD.", Category.RENDER);
	}

	@Override
	public void onHud(GuiGraphicsExtractor g, Minecraft mc) {
		Font font = mc.font;
		int screenW = mc.getWindow().getGuiScaledWidth();
		boolean left = position.is("Left");

		List<Module> list = ModuleManager.getModules().stream()
				.filter(Module::isEnabled)
				.sorted(Comparator.comparingInt((Module m) -> -font.width(m.getName())))
				.toList();

		int y = 3;
		for (Module m : list) {
			int tw = font.width(m.getName());
			int x = left ? 6 : screenW - 6 - tw;
			if (background.get()) g.fill(x - 3, y - 1, x + tw + 3, y + 10, 0x90000000);
			if (bar.get()) {
				if (left) g.fill(x - 3, y - 1, x - 2, y + 10, Theme.accent());
				else g.fill(x + tw + 2, y - 1, x + tw + 3, y + 10, Theme.accent());
			}
			RenderUtil.text(g, font, m.getName(), x, y, 0xFFFFFFFF, true);
			y += 11;
		}
	}
}
