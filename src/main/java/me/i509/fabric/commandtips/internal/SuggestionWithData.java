package me.i509.fabric.commandtips.internal;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.api.suggestion.SuggestionData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class SuggestionWithData extends Suggestion {
	private final SuggestionData<?> data;

	public SuggestionWithData(StringRange range, String text, SuggestionData<?> data) {
		super(range, text);
		this.data = data;
	}

	public SuggestionWithData(StringRange range, String text, Message tooltip, SuggestionData<?> data) {
		super(range, text, tooltip);
		this.data = data;
	}

	public SuggestionData<?> getData() {
		return this.data;
	}
}
