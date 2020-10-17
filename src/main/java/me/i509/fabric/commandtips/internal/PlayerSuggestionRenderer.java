package me.i509.fabric.commandtips.internal;

import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.api.render.SuggestionRenderer;
import me.i509.fabric.commandtips.api.suggestion.PlayerSuggestionData;

import net.minecraft.client.util.math.MatrixStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class PlayerSuggestionRenderer implements SuggestionRenderer<PlayerSuggestionData> {
	@Override
	public void render(Suggestion suggestion, PlayerSuggestionData data, MatrixStack matrices, int suggestionEntry, int x, int y, int mouseX, int mouseY) {
	}
}
