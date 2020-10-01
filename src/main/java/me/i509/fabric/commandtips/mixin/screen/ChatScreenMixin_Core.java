package me.i509.fabric.commandtips.mixin.screen;

import me.i509.fabric.commandtips.bridge.ChatScreenBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin_Core extends ScreenMixin_Core implements ChatScreenBridge {
	@Shadow
	protected TextFieldWidget chatField;

	@Shadow
	private String originalChatText;

	@Unique
	protected boolean ctp_shouldCache = true;

	@Inject(at = @At("TAIL"), method = "init")
	private void ctp_loadCachedPreviousCommand(CallbackInfo ci) {
		// TODO rethink the command caching conecept
	}

	/**
	 * @reason Cache command on close.
	 */
	@Override
	protected void ctp_onClose(CallbackInfo ci) {
		// TODO rethink the command caching conecept
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;sendMessage(Ljava/lang/String;)V"), method = "keyPressed")
	private void ctp_onSendCommand(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		// TODO rethink the command caching conecept
	}
}
