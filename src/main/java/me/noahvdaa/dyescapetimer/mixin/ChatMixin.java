package me.noahvdaa.dyescapetimer.mixin;

import me.noahvdaa.dyescapetimer.DyescapeTimer;
import me.noahvdaa.dyescapetimer.ServerCheckUtil;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHudListener.class)
public class ChatMixin {
	@Inject(method = {"onChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;)V"}, at = {@At("HEAD")}, cancellable = true)
	public void postSay(MessageType type, Text textComponent, UUID uuid, CallbackInfo ci) {
		// Not on dyescape.
		if (!ServerCheckUtil.isOnDyescape()) return;

		// No cat selected.
		if (DyescapeTimer.getInstance().activeRun == null) return;

		// Get clean version of message.
		String message = textComponent.getString().replaceAll("\n", "").trim().replaceAll(" +", " ");

		if (!DyescapeTimer.getInstance().isActive) {
			if (message.equals(DyescapeTimer.getInstance().activeRun.getStartTrigger())) {
				DyescapeTimer.getInstance().isActive = true;
				DyescapeTimer.getInstance().startedAt = System.currentTimeMillis();
			}
		} else {
			if (message.equals(DyescapeTimer.getInstance().activeRun.getEndTrigger())) {
				DyescapeTimer.getInstance().isActive = false;
				DyescapeTimer.getInstance().stoppedAt = System.currentTimeMillis();
			}
		}
	}
}
