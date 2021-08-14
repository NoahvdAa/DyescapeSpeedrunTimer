package me.noahvdaa.dyescapetimer;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class SelectRunGUI extends LightweightGuiDescription {
	final DyescapeTimer mod;

	public SelectRunGUI(DyescapeTimer mod) {
		this.mod = mod;

		// Create the panel.
		WGridPanel gridPanel = new WGridPanel();
		gridPanel.setSize(128, 128);
		setRootPanel(gridPanel);

		WText instruction = new WText(new LiteralText("§0§lSelect a speedrun category:"));
		gridPanel.add(instruction, 0, 0, 10, 1);

		// Add speedrun category buttons.
		int y = 0;
		for (SpeedrunCategory category : SpeedrunCategory.values()) {
			y += 2;
			WButton button = new WButton(new LiteralText(category.getHumanName()));
			button.setOnClick(() -> {
				mod.activeRun = category;
				// Reset all timing.
				mod.isActive = false;
				mod.startedAt = null;
				mod.stoppedAt = null;
				// Close the GUI.
				MinecraftClient client = MinecraftClient.getInstance();
				client.openScreen(null);
				// Send information message.
				LiteralText infoMessage = new LiteralText("§eRun category selected: §b" + category.getHumanName() + "§e. The timer will start when you create a new profile by jumping in the portal in the lobby, and will end " + category.getHumanEndTrigger() + ".");
				client.inGameHud.getChatHud().addMessage(infoMessage);
			});
			gridPanel.add(button, 0, y, 10, 1);
		}

		gridPanel.validate(this);
	}
}