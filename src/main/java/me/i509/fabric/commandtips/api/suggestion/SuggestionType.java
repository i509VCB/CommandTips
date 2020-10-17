package me.i509.fabric.commandtips.api.suggestion;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class SuggestionType<D extends SuggestionData<D>> {
	public static <D extends SuggestionData<D>> SuggestionType<D> of() {
		return new SuggestionType<>();
	}

	private SuggestionType() {
	}
}
