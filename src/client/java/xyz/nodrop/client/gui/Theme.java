package xyz.nodrop.client.gui;

import xyz.nodrop.client.module.ModuleManager;
import xyz.nodrop.client.module.impl.GuiSettingsModule;

public final class Theme {
	public static final int ROW = 0xFF17171D;
	public static final int ROW_HOVER = 0xFF21212B;
	public static final int HEADER = 0xFF1D1D26;
	public static final int SETTINGS_BG = 0xFF111116;
	public static final int TEXT = 0xFFEEEEF4;
	public static final int TEXT_DIM = 0xFF8B8B99;
	public static final int OFF = 0xFF3A3A46;
	public static final int TRACK = 0xFF2A2A34;

	private static GuiSettingsModule settings;

	private Theme() {}

	public static GuiSettingsModule settings() {
		if (settings == null) settings = ModuleManager.get(GuiSettingsModule.class);
		return settings;
	}

	public static int accent() {
		return switch (settings().accent.get()) {
			case "Blue" -> 0xFF4C9BFF;
			case "Green" -> 0xFF3DDC84;
			case "Red" -> 0xFFFF5A5F;
			case "Pink" -> 0xFFFF6FB5;
			case "Orange" -> 0xFFFFA23A;
			default -> 0xFF9D6CFF; // Purple
		};
	}

	public static int lerp(int a, int b, float t) {
		t = Math.max(0f, Math.min(1f, t));
		int aa = (a >>> 24) & 255, ar = (a >> 16) & 255, ag = (a >> 8) & 255, ab = a & 255;
		int ba = (b >>> 24) & 255, br = (b >> 16) & 255, bg = (b >> 8) & 255, bb = b & 255;
		int ra = (int) (aa + (ba - aa) * t);
		int rr = (int) (ar + (br - ar) * t);
		int rg = (int) (ag + (bg - ag) * t);
		int rb = (int) (ab + (bb - ab) * t);
		return (ra << 24) | (rr << 16) | (rg << 8) | rb;
	}

	public static int withAlpha(int color, int alpha) {
		return (alpha << 24) | (color & 0x00FFFFFF);
	}

	/** Plynne dazenie wartosci do celu; k = min(1, dt * speed). */
	public static float approach(float current, float target, float k) {
		float next = current + (target - current) * Math.min(1f, k);
		return Math.abs(target - next) < 0.003f ? target : next;
	}
}
