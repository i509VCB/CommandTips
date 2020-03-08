package me.i509.fabric.commandtips.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.util.Formatting;

public class ColoredSuggestion extends Suggestion {
	private final Formatting formatting;

	public ColoredSuggestion(StringRange range, String text, Formatting formatting) {
		super(range, text);
		this.formatting = formatting;
	}

	public ColoredSuggestion(StringRange range, String text, Message tooltip, Formatting formatting) {
		super(range, text, tooltip);
		this.formatting = formatting;
	}

	public Formatting getFormatting() {
		return this.formatting;
	}
}
