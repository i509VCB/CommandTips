package me.i509.fabric.commandtips.internal;

import me.i509.fabric.commandtips.api.config.CommandTipsConfig;

import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
final class CommandTipsConfigImpl implements CommandTipsConfig {
	private TextColor cachedEntitySuggestionColor = TextColor.fromFormatting(Formatting.GREEN);

	public CommandTipsConfigImpl() {
	}

	@Override
	public TextColor getCachedEntitySuggestionColor() {
		return this.cachedEntitySuggestionColor;
	}

	void setCachedEntitySuggestionColor(TextColor color) {
		this.cachedEntitySuggestionColor = color;
	}
}
