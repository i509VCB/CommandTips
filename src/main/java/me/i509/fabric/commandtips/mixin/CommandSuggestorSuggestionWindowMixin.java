package me.i509.fabric.commandtips.mixin;

import java.util.List;

import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.api.render.SuggestionRenderer;
import me.i509.fabric.commandtips.api.render.SuggestionRendererRegistry;
import me.i509.fabric.commandtips.api.suggestion.ColorSuggestionData;
import me.i509.fabric.commandtips.api.suggestion.SuggestionData;
import me.i509.fabric.commandtips.api.suggestion.SuggestionTypes;
import me.i509.fabric.commandtips.internal.SuggestionWithData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.util.Rect2i;
import net.minecraft.client.util.math.MatrixStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(CommandSuggestor.SuggestionWindow.class)
abstract class CommandSuggestorSuggestionWindowMixin {
	@Shadow
	@Final
	private List<Suggestion> field_25709;
	@Shadow
	private int inWindowIndex;
	@Shadow
	@Final
	private Rect2i area;

	@Unique
	private Integer cacheColor;

	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;II)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void renderSuggestions(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci, int suggestionListSize) {
		for(int entry = 0; entry < suggestionListSize; entry++) {
			final Suggestion suggestion = this.field_25709.get(entry + this.inWindowIndex);

			if (suggestion instanceof SuggestionWithData) {
				final SuggestionData<?> data = ((SuggestionWithData) suggestion).getData();
				//noinspection rawtypes - Generics are pain
				final SuggestionRenderer renderer = SuggestionRendererRegistry.getRenderer(data.getType());

				if (renderer != null) {
					//noinspection unchecked - Generics are pain
					renderer.render(suggestion, data, matrices, entry, this.area.getX() - 16, this.area.getY() + (12 * entry), mouseX, mouseY);
				}
			}
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestion;getText()Ljava/lang/String;"), method = "render")
	private String cacheTextColor(Suggestion suggestion) {
		if (suggestion instanceof SuggestionWithData) {
			final ColorSuggestionData suggestionData = SuggestionData.get(SuggestionTypes.COLOR, suggestion);

			if (suggestionData != null) {
				this.cacheColor = suggestionData.getColor().getRgb();
			} else {
				this.cacheColor = null;
			}
		} else {
			this.cacheColor = null;
		}

		return suggestion.getText();
	}

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"), method = "render", index = 4)
	private int modifyTextColor(int color) {
		if (this.cacheColor != null && color == -5592406) {
			return this.cacheColor;
		}

		return color;
	}
}
