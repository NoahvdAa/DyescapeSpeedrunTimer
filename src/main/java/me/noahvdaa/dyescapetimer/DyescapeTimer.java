package me.noahvdaa.dyescapetimer;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import me.noahvdaa.dyescapetimer.util.ServerCheckUtil;
import me.noahvdaa.dyescapetimer.util.TimeUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class DyescapeTimer implements ClientModInitializer {
	private static DyescapeTimer instance;
	private final SelectRunGUI selectRunGUI = new SelectRunGUI(this);
	public SpeedrunCategory activeRun;
	public Long startedAt;
	public Long stoppedAt;
	public boolean isActive = false;

	@Override
	public void onInitializeClient() {
		instance = this;

		// Register the keybinding to open the select run GUI.
		KeyBinding selectRunGUIKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.dyescapetimer.selectrungui", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_O, // The keycode of the key
				"category.dyescapetimer" // The translation key of the keybinding's category.
		));

		// Respond to keybinding for opening the select run gui.
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (selectRunGUIKeyBinding.wasPressed()) {
				if (!ServerCheckUtil.isOnDyescape()) {
					MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§cYou must be on Dyescape to use the Dyescape speedrun timer."));
					return;
				}

				client.openScreen(new CottonClientScreen(selectRunGUI));
			}
		});

		ClientLoginConnectionEvents.INIT.register((a, b) -> {
			// Don't continue if we aren't on Dyescape.
			if (ServerCheckUtil.isOnDyescape())
				return;
			// Looks like we aren't on dyescape, reset everything.
			isActive = false;
			activeRun = null;
			startedAt = null;
			stoppedAt = null;
		});

		// Renderer for timer.
		HudRenderCallback.EVENT.register((a, b) -> {
			// Not running anything.
			if (activeRun == null) return;

			TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

			renderer.draw(new MatrixStack(), new LiteralText("§6§l" + activeRun.getHumanName()), 25f, 25f, 0xFFF);

			String time = "00:00.000";
			// Have we started running yet?
			if (startedAt != null) {
				time = TimeUtil.format((stoppedAt == null ? System.currentTimeMillis() : stoppedAt) - startedAt);
			}
			renderer.draw(new MatrixStack(), new LiteralText((isActive ? "§a" : "§6") + time), 25f, 40f, 0xFFF);
		});
	}

	public static DyescapeTimer getInstance() {
		return instance;
	}
}
