package me.i509.fabric.commandtips.internal;

import java.util.UUID;

import me.i509.fabric.commandtips.api.config.CommandTipsConfig;
import me.i509.fabric.commandtips.api.render.SuggestionRendererRegistry;
import me.i509.fabric.commandtips.api.suggestion.SuggestionTypes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public final class CommandTipsClientMod implements ClientModInitializer,
		ClientLifecycleEvents.ClientStarted,
		ClientTickEvents.EndTick,
		ClientEntityEvents.Unload
{
	private static final CachedEntityTracker entityTracker = new CachedEntityTracker();
	private static CommandTipsConfigImpl config = new CommandTipsConfigImpl();
	private MinecraftClient client;

	public static CachedEntityTracker getEntityTracker() {
		return entityTracker;
	}

	public static CommandTipsConfig getConfig() {
		return config;
	}

	@Override
	public void onInitializeClient() {
		// TODO: Load config

		ClientLifecycleEvents.CLIENT_STARTED.register(this);
		ClientTickEvents.END_CLIENT_TICK.register(this);
		ClientEntityEvents.ENTITY_UNLOAD.register(this);

		this.registerBuiltinSuggestionRenderers();
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

					entityTracker.startTracking(entityUUID, entityType);
					this.client.player.sendMessage(new TranslatableText("commandtips.cmd.cached", new TranslatableText(entityType.getTranslationKey()), entityUUID.toString()).formatted(Formatting.GRAY), false);
				}
			}
		}
	}

	@Override
	public void onUnload(Entity entity, ClientWorld world) {
		entityTracker.stopTracking(entity);
	}

	private void registerBuiltinSuggestionRenderers() {
		SuggestionRendererRegistry.register(SuggestionTypes.PLAYER, new PlayerSuggestionRenderer());
	}
}
