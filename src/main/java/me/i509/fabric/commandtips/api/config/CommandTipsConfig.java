package me.i509.fabric.commandtips.api.config;

import net.minecraft.text.TextColor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface CommandTipsConfig {
	TextColor getCachedEntitySuggestionColor();
}
