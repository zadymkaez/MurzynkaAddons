package xyz.nodrop.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.module.setting.BooleanSetting;

public class BooleanComponent extends SettingComponent {
	private final BooleanSetting setting;
	private float anim;

	public BooleanComponent(BooleanSetting setting) {
		this.setting = setting;
		this.anim = setting.get() ? 1f : 0f;
	}

	@Override
	protected void draw(GuiGraphicsExtractor g, Font font, int mx, int my, float k) {
		anim = Theme.approach(anim, setting.get() ? 1f : 0f, k);
		if (hit(mx, my)) g.fill(x, y, x + w, y + height(), 0x14FFFFFF);
		RenderUtil.text(g, font, setting.getName(), x + 8, y + 4, Theme.lerp(Theme.TEXT_DIM, Theme.TEXT, anim), false);
		RenderUtil.toggleSwitch(g, x + w - 22, y + 4, anim);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0 && hit(mx, my)) {
			setting.toggle();
			return true;
		}
		return false;
	}
}
