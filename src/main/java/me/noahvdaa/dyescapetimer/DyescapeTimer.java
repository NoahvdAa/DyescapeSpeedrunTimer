package me.noahvdaa.dyescapetimer;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class DyescapeTimer implements ModInitializer {
	private static DyescapeTimer instance;
	public SpeedrunCategory activeRun;
	public Long startedAt;
	public Long stoppedAt;
	public boolean isActive = false;

	@Override
	public void onInitialize() {
		instance = this;

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			if (dedicated) return;

			ClientCommandManager.DISPATCHER.register(literal("startrun").requires(DyescapeTimer::checkOnDyescape).executes(context -> {
				LiteralText helpText = new LiteralText("§6Dyescape Speedrun Timer\nAvailable categories:");

				for (SpeedrunCategory category : SpeedrunCategory.values()) {
					LiteralText catReadme = new LiteralText("\n§e  - " + category.getHumanName());

					catReadme.setStyle(catReadme.getStyle()
							.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/startrun " + category.name()))
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click to select."))));

					helpText.append(catReadme);
				}

				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(helpText);

				return 1;
			}).then(argument("category", StringArgumentType.word()).executes(context -> {
				String category = StringArgumentType.getString(context, "category");
				SpeedrunCategory cat;

				try {
					cat = SpeedrunCategory.valueOf(category);
				} catch (Exception e) {
					Text errorMessage = new LiteralText("§cInvalid category. Run /startrun for a full list of categories.");
					MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(errorMessage);
					return 1;
				}

				activeRun = cat;
				startedAt = null;
				stoppedAt = null;
				isActive = false;

				Text startText = new LiteralText("§aTimer for §l" + cat.getHumanName() + " §awill start as soon as you create a new profile by jumping in the portal in the lobby.");
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(startText);

				return 1;
			})));
		});

		HudRenderCallback.EVENT.register((a, b) -> {
			if (activeRun == null) return;
			TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

			renderer.draw(new MatrixStack(), new LiteralText("§6§l" + activeRun.getHumanName()), 25f, 25f, 0xFFF);

			String time = "00:00.000";
			if (startedAt != null) {
				time = TimeUtil.format((stoppedAt == null ? System.currentTimeMillis() : stoppedAt) - startedAt);
			}
			renderer.draw(new MatrixStack(), new LiteralText((isActive ? "§a" : "§6") + time), 25f, 40f, 0xFFF);
		});
	}

	private static boolean checkOnDyescape(FabricClientCommandSource source) {
		return ServerCheckUtil.isOnDyescape();
	}

	public static DyescapeTimer getInstance() {
		return instance;
	}
}
