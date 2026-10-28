package xyz.nodrop.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.module.setting.NumberSetting;

import java.util.Locale;

public class NumberComponent extends SettingComponent {
	private final NumberSetting setting;
	private boolean dragging;
	private float shown;

	public NumberComponent(NumberSetting setting) {
		this.setting = setting;
		this.shown = percent();
	}

	@Override
	public int height() { return 21; }

	private float percent() {
		return (float) ((setting.get() - setting.getMin()) / (setting.getMax() - setting.getMin()));
	}

	@Override
	protected void draw(GuiGraphicsExtractor g, Font font, int mx, int my, float k) {
		int trackX = x + 8, trackW = w - 16;
		if (dragging) {
			double p = Math.max(0, Math.min(1, (mx - trackX) / (double) trackW));
			setting.set(setting.getMin() + p * (setting.getMax() - setting.getMin()));
		}
		shown = Theme.approach(shown, percent(), k * 1.5f);

		RenderUtil.text(g, font, setting.getName(), x + 8, y + 3, Theme.TEXT_DIM, false);
		double v = setting.get();
		String value = setting.getStep() % 1 == 0 ? String.valueOf((int) Math.round(v)) : String.format(Locale.ROOT, "%.2f", v);
		RenderUtil.text(g, font, value, x + w - 8 - font.width(value), y + 3, Theme.TEXT, false);

		int ty = y + height() - 6;
		g.fill(trackX, ty, trackX + trackW, ty + 3, Theme.TRACK);
		int filled = Math.round(trackW * shown);
		g.fill(trackX, ty, trackX + filled, ty + 3, Theme.accent());
		RenderUtil.roundRect(g, trackX + filled - 2, ty - 1, 5, 5, 2, 0xFFFFFFFF);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0 && hit(mx, my)) {
			dragging = true;
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(int button) {
		if (button == 0) dragging = false;
	}
}
