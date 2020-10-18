package me.i509.fabric.commandtips.mixin;

import me.i509.fabric.commandtips.internal.CommandTipsClientMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
abstract class ClientPlayNetworkHandlerMixin {
	@Inject(method = "onPlayerList", at = @At("TAIL"))
	private void handlePlayerListPacket(PlayerListS2CPacket packet, CallbackInfo ci) {
		for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
			switch (packet.getAction()) {
			case ADD_PLAYER:
			case UPDATE_GAME_MODE:
			case UPDATE_LATENCY:
			case UPDATE_DISPLAY_NAME:
				break;
			case REMOVE_PLAYER:
				CommandTipsClientMod.getPlayerModelTracker().stopTrackingPlayer(entry.getProfile().getId());
				break;
			}
		}
	}
}
