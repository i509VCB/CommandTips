package me.i509.fabric.commandtips.mixin;

import me.i509.fabric.commandtips.CommandTipsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin_TargetedEntity {
	@Shadow @Final private MinecraftClient client;

	@Inject(at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, ordinal = 2), method = "updateTargetedEntity(F)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void commandTips_updateTargetEntity(
		float tickDelta, CallbackInfo ci, Entity cameraEntity, double playerReachDistance, Vec3d cameraPosition, boolean isExtendedReach, int i, double e, Vec3d cameraRotationVector,
		Vec3d endReachPosition, float boxExpansionAmount, Box box, EntityHitResult entityHitResult, Entity targetedEntity, Vec3d hitResultPosition, double hitResultDistanceFromCamera) {
		if (CommandTipsClient.getInstance() != null && CommandTipsClient.getInstance().getConfig().targetItemEntity) {
			if (targetedEntity instanceof ItemEntity) {
				this.client.targetedEntity = targetedEntity;
			}
		}
	}
}
