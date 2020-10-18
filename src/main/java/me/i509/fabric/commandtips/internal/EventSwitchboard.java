package me.i509.fabric.commandtips.internal;

import java.util.UUID;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
final class EventSwitchboard implements ClientEntityEvents.Load,
		ClientEntityEvents.Unload,
		ClientLifecycleEvents.ClientStarted,
		ClientTickEvents.EndTick
{
	private MinecraftClient client;

	EventSwitchboard() {
		ClientEntityEvents.ENTITY_LOAD.register(this);
		ClientEntityEvents.ENTITY_UNLOAD.register(this);
		ClientLifecycleEvents.CLIENT_STARTED.register(this);
		ClientTickEvents.END_CLIENT_TICK.register(this);
	}

	@Override
	public void onClientStarted(MinecraftClient client) {
		this.client = client;
	}

	@Override
	public void onEndTick(MinecraftClient minecraftClient) {
		// Keybinding logic
		if (CommandTipsRegistry.CACHE_ENTITY.wasPressed()) {
			if (this.client.getNetworkHandler() != null) {
				final Entity entity = this.client.targetedEntity;

				if (entity != null && this.client.player != null) {
					UUID entityUUID = entity.getUuid();
					EntityType<?> entityType = entity.getType();

					CommandTipsClientMod.getEntityTracker().startTrackingCursorEntity(entityUUID, entityType);
					this.client.player.sendMessage(new TranslatableText("commandtips.cmd.cached", new TranslatableText(entityType.getTranslationKey()), entityUUID.toString()).formatted(Formatting.GRAY), false);
				}
			}
		}
	}

	@Override
	public void onLoad(Entity entity, ClientWorld world) {
		if (entity instanceof PlayerEntity) {
			// Player is being tracked by client; we can use the player in world for model parts
			CommandTipsClientMod.getPlayerModelTracker().stopTrackingPlayer(entity.getUuid());
		}
	}

	@Override
	public void onUnload(Entity entity, ClientWorld world) {
		CommandTipsClientMod.getEntityTracker().stopTrackingCursorEntity(entity);

		if (entity instanceof PlayerEntity) {
			CommandTipsClientMod.getPlayerModelTracker().startTrackingPlayer(((PlayerEntity) entity));
		}
	}
}
