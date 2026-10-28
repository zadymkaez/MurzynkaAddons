package xyz.nodrop.client.gui;

import xyz.nodrop.client.module.Category;

import java.util.HashMap;
import java.util.Map;

/** Pozycje paneli: {x, y, open(0/1)}. Trzymane statycznie, zeby config mogl je zapisac. */
public final class PanelLayout {
	private static final Map<String, float[]> STATE = new HashMap<>();

	private PanelLayout() {}

	public static float[] get(Category category) {
		return STATE.computeIfAbsent(category.name(),
				k -> new float[]{16 + category.ordinal() * (PanelComponent.WIDTH + 12), 16, 1f});
	}
}
