package me.i509.fabric.commandtips.api.render;

import java.util.IdentityHashMap;
import java.util.Map;

import me.i509.fabric.commandtips.api.suggestion.SuggestionData;
import me.i509.fabric.commandtips.api.suggestion.SuggestionType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class SuggestionRendererRegistry {
	private static final Map<SuggestionType<?>, SuggestionRenderer<?>> RENDERERS = new IdentityHashMap<>();

	public static <D extends SuggestionData<D>> void register(SuggestionType<D> type, SuggestionRenderer<D> renderer) {
		if (RENDERERS.putIfAbsent(type, renderer) != null) {
			throw new RuntimeException("Attempted to override renderer");
		}
	}

	public static <D extends SuggestionData<D>> SuggestionRenderer<D> getRenderer(SuggestionType<D> type) {
		//noinspection unchecked
		return (SuggestionRenderer<D>) RENDERERS.get(type);
	}
}
