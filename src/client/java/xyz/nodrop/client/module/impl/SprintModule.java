package xyz.nodrop.client.module.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import xyz.nodrop.client.module.Category;
import xyz.nodrop.client.module.Module;
import xyz.nodrop.client.module.setting.BooleanSetting;

public class SprintModule extends Module {
	private final BooleanSetting hungerCheck = addSetting(new BooleanSetting("Hunger check", true));

	public SprintModule() {
		super("Sprint", "Automatically sprints while you move forward.", Category.MOVEMENT);
	}

	@Override
	public void onTick(Minecraft client) {
		LocalPlayer p = client.player;
		if (p == null || !client.options.keyUp.isDown()) return;
		if (p.isShiftKeyDown() || p.isUsingItem() || p.horizontalCollision) return;
		if (hungerCheck.get() && p.getFoodData().getFoodLevel() <= 6) return;
		p.setSprinting(true);
	}
}
