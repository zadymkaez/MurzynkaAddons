package xyz.nodrop.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class SettingComponent {
	protected int x, y, w;

	public int height() { return 16; }

	public final void render(GuiGraphicsExtractor g, Font font, int x, int y, int w, int mx, int my, float k) {
		this.x = x;
		this.y = y;
		this.w = w;
		draw(g, font, mx, my, k);
	}

	protected abstract void draw(GuiGraphicsExtractor g, Font font, int mx, int my, float k);

	public boolean mouseClicked(double mx, double my, int button) { return false; }

	public void mouseReleased(int button) {}

	protected boolean hit(double mx, double my) {
		return mx >= x && mx < x + w && my >= y && my < y + height();
	}
}
