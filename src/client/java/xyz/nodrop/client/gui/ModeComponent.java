package xyz.nodrop.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.module.setting.ModeSetting;

public class ModeComponent extends SettingComponent {
	private final ModeSetting setting;

	public ModeComponent(ModeSetting setting) {
		this.setting = setting;
	}

	@Override
	protected void draw(GuiGraphicsExtractor g, Font font, int mx, int my, float k) {
		if (hit(mx, my)) g.fill(x, y, x + w, y + height(), 0x14FFFFFF);
		RenderUtil.text(g, font, setting.getName(), x + 8, y + 4, Theme.TEXT_DIM, false);
		String value = setting.get();
		RenderUtil.text(g, font, value, x + w - 6 - font.width(value), y + 4, Theme.accent(), false);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (!hit(mx, my)) return false;
		if (button == 0) setting.cycle(true);
		else if (button == 1) setting.cycle(false);
		else return false;
		return true;
	}
}
