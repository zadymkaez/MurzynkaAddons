package xyz.nodrop.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import xyz.nodrop.client.config.ConfigManager;
import xyz.nodrop.client.module.Category;
import xyz.nodrop.client.module.Module;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
	private static ClickGuiScreen instance;

	private final List<PanelComponent> panels = new ArrayList<>();
	private float openAnim;
	private long lastNanos;

	public static ClickGuiScreen get() {
		if (instance == null) instance = new ClickGuiScreen();
		return instance;
	}

	private ClickGuiScreen() {
		super(Component.literal("Click GUI"));
		for (Category c : Category.values()) {
			PanelComponent p = new PanelComponent(c);
			if (!p.isEmpty()) panels.add(p);
		}
	}

	@Override
	protected void init() {
		openAnim = 0f;
		lastNanos = System.nanoTime();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
		long now = System.nanoTime();
		float dt = Math.min((now - lastNanos) / 1_000_000_000f, 0.1f);
		lastNanos = now;
		float k = Math.min(1f, dt * Theme.settings().animSpeed.floatValue());
		openAnim = Theme.approach(openAnim, 1f, k);

		if (Theme.settings().dimBackground.get()) {
			g.fill(0, 0, this.width, this.height, Math.round(0x90 * openAnim) << 24);
		}

		for (PanelComponent p : panels) p.render(g, this.minecraft.font, mouseX, mouseY, k);

		if (Theme.settings().descriptions.get()) {
			Module hovered = null;
			for (int i = panels.size() - 1; i >= 0 && hovered == null; i--) {
				hovered = panels.get(i).hoveredModule(mouseX, mouseY);
			}
			if (hovered != null) {
				String text = hovered.getDescription();
				int tw = this.minecraft.font.width(text);
				int bx = (this.width - tw) / 2 - 6, by = this.height - 26;
				RenderUtil.roundRect(g, bx, by, tw + 12, 16, 4, 0xE0111116);
				g.fill(bx, by + 13, bx + tw + 12, by + 14, Theme.withAlpha(Theme.accent(), 200));
				RenderUtil.text(g, this.minecraft.font, text, bx + 6, by + 4, Theme.TEXT, false);
			}
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		for (int i = panels.size() - 1; i >= 0; i--) {
			PanelComponent p = panels.get(i);
			if (p.mouseClicked(event.x(), event.y(), event.button())) {
				panels.remove(i);
				panels.add(p); // na wierzch
				return true;
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		for (PanelComponent p : panels) p.mouseReleased(event.button());
		return super.mouseReleased(event);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
		return true; // przeciaganie obslugujemy w render() na podstawie pozycji myszy
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		for (PanelComponent p : panels) p.moveBy((float) scrollY * 20f);
		return true;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			this.onClose();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public void onClose() {
		ConfigManager.save();
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
