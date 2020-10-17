package me.i509.fabric.commandtips.api.suggestion;

import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.internal.SuggestionWithData;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface SuggestionData<D extends SuggestionData<D>> {
	@Nullable
	static <D extends SuggestionData<D>> D get(SuggestionType<D> type, Suggestion suggestion) {
		if (suggestion instanceof SuggestionWithData) {
			final SuggestionData<?> data = ((SuggestionWithData) suggestion).getData();

			if (data.getType() == type) {
				return (D) data;
			}
		}

		return null;
	}

	SuggestionType<D> getType();
}
