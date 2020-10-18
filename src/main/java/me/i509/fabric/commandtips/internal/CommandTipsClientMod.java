package me.i509.fabric.commandtips.internal;

import me.i509.fabric.commandtips.api.config.CommandTipsConfig;
import me.i509.fabric.commandtips.api.render.SuggestionRendererRegistry;
import me.i509.fabric.commandtips.api.suggestion.SuggestionTypes;
import me.i509.fabric.commandtips.internal.suggestion.ItemSuggestionRenderer;
import me.i509.fabric.commandtips.internal.suggestion.PlayerSuggestionRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class CommandTipsClientMod implements ClientModInitializer {
	private static final CachedEntityTracker entityTracker = new CachedEntityTracker();
	private static final PlayerModelTracker playerModelTracker = new PlayerModelTracker();
	private static CommandTipsConfigImpl config = new CommandTipsConfigImpl();

	public static CachedEntityTracker getEntityTracker() {
		return entityTracker;
	}

	public static PlayerModelTracker getPlayerModelTracker() {
		return playerModelTracker;
	}

	public static CommandTipsConfig getConfig() {
		return config;
	}

	@Override
	public void onInitializeClient() {
		new EventSwitchboard();
		this.registerBuiltinSuggestionRenderers();
	}

	private void registerBuiltinSuggestionRenderers() {
		SuggestionRendererRegistry.register(SuggestionTypes.PLAYER, new PlayerSuggestionRenderer());
		SuggestionRendererRegistry.register(SuggestionTypes.ITEM, new ItemSuggestionRenderer());
	}
}
