package me.i509.fabric.commandtips.api.render;

import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.api.suggestion.SuggestionData;

import net.minecraft.client.util.math.MatrixStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SuggestionRenderer<D extends SuggestionData<D>> {
	void render(Suggestion suggestion, D data, MatrixStack matrices, int suggestionEntry, int x, int y, int mouseX, int mouseY);
}
