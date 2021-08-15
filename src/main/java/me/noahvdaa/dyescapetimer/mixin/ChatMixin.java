package me.noahvdaa.dyescapetimer.mixin;

import me.noahvdaa.dyescapetimer.DyescapeTimer;
import me.noahvdaa.dyescapetimer.util.ServerCheckUtil;
import me.noahvdaa.dyescapetimer.util.TimeUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHudListener.class)
public class ChatMixin {
	private final DyescapeTimer mod = DyescapeTimer.getInstance();

	@Inject(method = {"onChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V"}, at = {@At("HEAD")}, cancellable = true)
	public void postSay(MessageType type, Text textComponent, UUID uuid, CallbackInfo ci) {
		// Not on dyescape.
		if (!ServerCheckUtil.isOnDyescape()) return;

		// No cat selected.
		if (mod.activeRun == null) return;

		// Get clean version of message.
		String message = textComponent.getString().replaceAll("\n", "").trim().replaceAll(" +", " ");

		if (!mod.isActive) {
			// We haven't started yet (or are waiting for a reset), check for start trigger in chat.
			if (message.matches(mod.activeRun.getStartTrigger())) {
				mod.isActive = true;
				mod.startedAt = System.currentTimeMillis();
				mod.stoppedAt = null;
			}
		} else {
			// We have started, check for end trigger in chat.
			if (message.matches(mod.activeRun.getEndTrigger())) {
				mod.isActive = false;
				mod.stoppedAt = System.currentTimeMillis();

				// Congratulate with time.
				LiteralText infoMessage = new LiteralText("§eCongratulations! You finished your §b" + mod.activeRun.getHumanName() + "§e run in " + TimeUtil.format(mod.stoppedAt - mod.startedAt) + "!");
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(infoMessage);
			}
		}
	}
}
