package xyz.nodrop.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.module.Module;
import xyz.nodrop.client.module.setting.BooleanSetting;
import xyz.nodrop.client.module.setting.ModeSetting;
import xyz.nodrop.client.module.setting.NumberSetting;
import xyz.nodrop.client.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class ModuleComponent {
	public static final int HEIGHT = 18;

	private final Module module;
	private final List<SettingComponent> settings = new ArrayList<>();
	private boolean expanded;
	private float expandAnim, hoverAnim, toggleAnim;
	private int x, y, w;

	public ModuleComponent(Module module) {
		this.module = module;
		this.toggleAnim = module.isEnabled() ? 1f : 0f;
		for (Setting<?> s : module.getSettings()) {
			if (s instanceof BooleanSetting b) settings.add(new BooleanComponent(b));
			else if (s instanceof NumberSetting n) settings.add(new NumberComponent(n));
			else if (s instanceof ModeSetting m) settings.add(new ModeComponent(m));
		}
	}

	public Module getModule() { return module; }

	private int settingsFullHeight() {
		int h = 0;
		for (SettingComponent c : settings) h += c.height();
		return h;
	}

	private int settingsHeight() {
		return Math.round(settingsFullHeight() * expandAnim);
	}

	public int currentHeight() {
		return HEIGHT + settingsHeight();
	}

	public boolean isRowHovered(double mx, double my) {
		return mx >= x && mx < x + w && my >= y && my < y + HEIGHT;
	}

	public void render(GuiGraphicsExtractor g, Font font, int x, int y, int w, int mx, int my, float k) {
		this.x = x;
		this.y = y;
		this.w = w;
		int accent = Theme.accent();

		hoverAnim = Theme.approach(hoverAnim, isRowHovered(mx, my) ? 1f : 0f, k);
		toggleAnim = Theme.approach(toggleAnim, module.isEnabled() ? 1f : 0f, k);
		expandAnim = Theme.approach(expandAnim, expanded ? 1f : 0f, k * 0.8f);

		// wiersz modulu
		g.fill(x, y, x + w, y + HEIGHT, Theme.lerp(Theme.ROW, Theme.ROW_HOVER, hoverAnim));
		g.fill(x, y, x + 2, y + HEIGHT, Theme.withAlpha(accent, Math.round(255 * toggleAnim)));
		RenderUtil.text(g, font, module.getName(), x + 8, y + 5,
				Theme.lerp(Theme.TEXT_DIM, Theme.TEXT, Math.max(toggleAnim, hoverAnim * 0.6f)), false);

		int right = x + w - 6;
		if (module.isToggleable()) {
			RenderUtil.toggleSwitch(g, right - 16, y + 5, toggleAnim);
			right -= 22;
		}
		if (!settings.isEmpty()) {
			String sign = expanded ? "-" : "+";
			RenderUtil.text(g, font, sign, right - font.width(sign), y + 5, Theme.TEXT_DIM, false);
		}

		// ustawienia
		int sh = settingsHeight();
		if (sh > 0) {
			RenderUtil.scissor(g, x, y + HEIGHT, x + w, y + HEIGHT + sh);
			g.fill(x, y + HEIGHT, x + w, y + HEIGHT + sh, Theme.SETTINGS_BG);
			int cy = y + HEIGHT;
			for (SettingComponent c : settings) {
				c.render(g, font, x, cy, w, mx, my, k);
				cy += c.height();
			}
			g.fill(x, y + HEIGHT, x + 1, y + HEIGHT + sh, Theme.withAlpha(accent, 140));
			RenderUtil.endScissor(g);
		}
	}

	public boolean mouseClicked(double mx, double my, int button) {
		if (mx < x || mx >= x + w || my < y || my >= y + currentHeight()) return false;

		if (my < y + HEIGHT) {
			boolean hasSettings = !settings.isEmpty();
			if (button == 0 && module.isToggleable()) module.toggle();
			else if (hasSettings && (button == 1 || (button == 0 && !module.isToggleable()))) expanded = !expanded;
			return true;
		}
		if (expandAnim > 0.5f) {
			for (SettingComponent c : settings) {
				if (c.mouseClicked(mx, my, button)) return true;
			}
		}
		return true;
	}

	public void mouseReleased(int button) {
		for (SettingComponent c : settings) c.mouseReleased(button);
	}
}
