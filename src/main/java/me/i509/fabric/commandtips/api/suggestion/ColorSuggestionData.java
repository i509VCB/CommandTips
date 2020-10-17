package me.i509.fabric.commandtips.api.suggestion;

import net.minecraft.text.TextColor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ColorSuggestionData extends AbstractSuggestionData<ColorSuggestionData> {
	private final TextColor color;

	public ColorSuggestionData(TextColor color) {
		super(SuggestionTypes.COLOR);
		this.color = color;
	}

	public TextColor getColor() {
		return this.color;
	}
}
