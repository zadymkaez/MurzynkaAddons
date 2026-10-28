package xyz.nodrop.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import xyz.nodrop.client.module.Category;
import xyz.nodrop.client.module.Module;
import xyz.nodrop.client.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

public class PanelComponent {
	public static final int WIDTH = 120;
	public static final int HEADER_H = 20;
	private static final int PAD = 4;

	private final Category category;
	private final float[] state; // {x, y, open}
	private final List<ModuleComponent> modules = new ArrayList<>();
	private float openAnim;
	private boolean dragging;
	private double dragDX, dragDY;
	private int x, y, totalH;

	public PanelComponent(Category category) {
		this.category = category;
		this.state = PanelLayout.get(category);
		this.openAnim = state[2];
		for (Module m : ModuleManager.getByCategory(category)) modules.add(new ModuleComponent(m));
	}

	public boolean isEmpty() { return modules.isEmpty(); }

	public void moveBy(float dy) { state[1] += dy; }

	public void render(GuiGraphicsExtractor g, Font font, int mx, int my, float k) {
		if (dragging) {
			state[0] = (float) (mx - dragDX);
			state[1] = (float) (my - dragDY);
		}
		x = Math.round(state[0]);
		y = Math.round(state[1]);
		int accent = Theme.accent();

		openAnim = Theme.approach(openAnim, state[2] >= 0.5f ? 1f : 0f, k);

		int modsH = 0;
		for (ModuleComponent m : modules) modsH += m.currentHeight();
		int bodyH = Math.round(modsH * openAnim);
		int pad = Math.round(PAD * openAnim);
		totalH = HEADER_H + 1 + bodyH + pad;

		// cien + tlo
		RenderUtil.roundRect(g, x - 1, y - 1, WIDTH + 2, totalH + 2, 5, 0x55000000);
		RenderUtil.roundRect(g, x, y, WIDTH, totalH, 4, Theme.ROW);

		// naglowek
		RenderUtil.roundRect(g, x, y, WIDTH, HEADER_H, 4, Theme.HEADER);
		g.fill(x, y + HEADER_H - 4, x + WIDTH, y + HEADER_H, Theme.HEADER);
		g.fill(x, y + HEADER_H, x + WIDTH, y + HEADER_H + 1, accent);
		RenderUtil.boldCentered(g, font, category.displayName, x + WIDTH / 2, y + 6, Theme.TEXT);

		// moduly
		if (bodyH > 0) {
			int top = y + HEADER_H + 1;
			RenderUtil.scissor(g, x, top, x + WIDTH, top + bodyH);
			int cy = top;
			for (ModuleComponent m : modules) {
				m.render(g, font, x, cy, WIDTH, mx, my, k);
				cy += m.currentHeight();
			}
			RenderUtil.endScissor(g);
		}
	}

	private boolean inBounds(double mx, double my) {
		return mx >= x && mx < x + WIDTH && my >= y && my < y + totalH;
	}

	public boolean mouseClicked(double mx, double my, int button) {
		if (!inBounds(mx, my)) return false;
		if (my < y + HEADER_H) {
			if (button == 0) {
				dragging = true;
				dragDX = mx - state[0];
				dragDY = my - state[1];
			} else if (button == 1) {
				state[2] = state[2] >= 0.5f ? 0f : 1f;
			}
			return true;
		}
		if (state[2] >= 0.5f) {
			for (ModuleComponent m : modules) {
				if (m.mouseClicked(mx, my, button)) return true;
			}
		}
		return true;
	}

	public void mouseReleased(int button) {
		if (button == 0) dragging = false;
		for (ModuleComponent m : modules) m.mouseReleased(button);
	}

	public Module hoveredModule(double mx, double my) {
		if (!inBounds(mx, my) || openAnim < 0.5f) return null;
		for (ModuleComponent m : modules) {
			if (m.isRowHovered(mx, my)) return m.getModule();
		}
		return null;
	}
}
