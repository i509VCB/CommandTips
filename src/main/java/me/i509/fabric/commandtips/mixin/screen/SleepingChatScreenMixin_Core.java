package me.i509.fabric.commandtips.mixin.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SleepingChatScreen.class)
public abstract class SleepingChatScreenMixin_Core extends ChatScreenMixin_Core {
	@Inject(at = @At("TAIL"), method = "onClose")
	private void ctp_cachePreviousCommand(CallbackInfo ci) {
		// TODO rethink the command caching conecept
	}
}
