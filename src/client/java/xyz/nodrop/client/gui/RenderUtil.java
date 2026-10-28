package xyz.nodrop.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

/**
 * Wszystkie wywolania API renderowania Minecrafta sa tu w jednym miejscu,
 * zeby po update wersji poprawiac tylko ten plik.
 */
public final class RenderUtil {
	private RenderUtil() {}

	public static void roundRect(GuiGraphicsExtractor g, int x, int y, int w, int h, int r, int color) {
		r = Math.min(r, Math.min(w, h) / 2);
		if (r <= 0) {
			g.fill(x, y, x + w, y + h, color);
			return;
		}
		for (int i = 0; i < r; i++) {
			double dy = r - i - 0.5;
			int inset = (int) Math.round(r - Math.sqrt(r * r - dy * dy));
			g.fill(x + inset, y + i, x + w - inset, y + i + 1, color);
			g.fill(x + inset, y + h - 1 - i, x + w - inset, y + h - i, color);
		}
		g.fill(x, y + r, x + w, y + h - r, color);
	}

	public static void text(GuiGraphicsExtractor g, Font font, String s, int x, int y, int color, boolean shadow) {
		g.text(font, s, x, y, color, shadow);
	}

	public static void boldCentered(GuiGraphicsExtractor g, Font font, String s, int centerX, int y, int color) {
		Component c = Component.literal(s).withStyle(ChatFormatting.BOLD);
		g.text(font, c, centerX - font.width(c) / 2, y, color, false);
	}

	public static void scissor(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2) {
		g.enableScissor(x1, y1, x2, y2);
	}

	public static void endScissor(GuiGraphicsExtractor g) {
		g.disableScissor();
	}

	/** Maly przelacznik 16x8 z animowana galka (anim 0..1). */
	public static void toggleSwitch(GuiGraphicsExtractor g, int x, int y, float anim) {
		roundRect(g, x, y, 16, 8, 4, Theme.lerp(Theme.OFF, Theme.accent(), anim));
		int knobX = x + 1 + Math.round(8 * anim);
		roundRect(g, knobX, y + 1, 6, 6, 3, 0xFFFFFFFF);
	}
}
