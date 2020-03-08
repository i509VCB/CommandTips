package me.i509.fabric.commandtips.mixin.screen;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin_Core {
	/**
	 * @reason Override to inject into onClose
	 */
	@Inject(at = @At("TAIL"), method = "onClose")
	protected void ctp_onClose(CallbackInfo ci) {
	}
}
