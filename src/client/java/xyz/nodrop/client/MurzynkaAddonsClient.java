package xyz.nodrop.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import xyz.nodrop.client.config.ConfigManager;
import xyz.nodrop.client.gui.ClickGuiScreen;
import xyz.nodrop.client.module.ModuleManager;

public class MurzynkaAddonsClient implements ClientModInitializer {
	/** Musi byc zgodne z "id" w fabric.mod.json (i z folderem assets/<id>). */
	public static final String MOD_ID = "murzynkaaddons";

	private static KeyMapping openGuiKey;

	@Override
	public void onInitializeClient() {
		ModuleManager.init();
		ConfigManager.load();

		KeyMapping.Category category = KeyMapping.Category.register(
				Identifier.fromNamespaceAndPath(MOD_ID, "main"));

		openGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key." + MOD_ID + ".open_gui",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				category));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openGuiKey.consumeClick()) {
				client.setScreenAndShow(ClickGuiScreen.get());
			}
			ModuleManager.onTick(client);
		});

		HudElementRegistry.addLast(
				Identifier.fromNamespaceAndPath(MOD_ID, "hud"),
				(graphics, tickCounter) -> ModuleManager.onHud(graphics));

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ConfigManager.save());
	}
}
