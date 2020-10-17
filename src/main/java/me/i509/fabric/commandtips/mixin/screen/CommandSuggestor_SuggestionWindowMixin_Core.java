package me.i509.fabric.commandtips.mixin.screen;

import java.util.List;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
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
@Mixin(value = CommandSuggestor.SuggestionWindow.class, priority = 1001)
abstract class CommandSuggestor_SuggestionWindowMixin_Core {
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
					renderer.render(suggestion, data, matrices, entry, this.area.getX() - 16, this.area.getY() + (12 * entry) - 1, mouseX, mouseY);
				}
			}

			/* TODO: Remove old code
			if (suggestion instanceof ItemRenderableSuggestion) {
				final ItemStack stack = new ItemStack(((ItemRenderableSuggestion) suggestion).getItem());
				final BakedModel model = MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, null, MinecraftClient.getInstance().player);
				// Scale the item down for screen render then scale it back up
				final Vector3f scale = model.getTransformation().gui.scale;

				if (stack.getItem() instanceof SignItem) {
					scale.scale(0.55F);
				} else if (stack.getItem() instanceof BlockItem) {
					scale.scale(0.65F);
				} else {
					scale.scale(0.020F);
				}

				MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, this.area.getX() - 16, this.area.getY() + (12 * entry) - 1);

				if (stack.getItem() instanceof SignItem) { // Special handling for the signs
					scale.scale((1/0.55F));
				} else if (stack.getItem() instanceof BlockItem) {
					scale.scale((1F/0.65F));
				} else {
					scale.scale((1F/0.020F));
				}
			} else if (suggestion instanceof PlayerEntrySuggestion) {
				final PlayerEntrySuggestion playerSuggestion = ((PlayerEntrySuggestion) suggestion);
				// http://assets.mojang.com/SkinTemplates/4px_reference.png
				// Reference skin
				MinecraftClient.getInstance().getTextureManager().bindTexture(playerSuggestion.getSkinTexture());
				DrawableHelper.drawTexture(matrices, this.area.getX() - 16, this.area.getY() + (12 * entry) + 2, 8.0F, 8.0F, 8, 8, 64, 64);
				// We can only do so much if the player instance can't actually be found, so we do our best to get the visible skin parts.
				final PlayerEntity player = MinecraftClient.getInstance().world.getPlayerByUuid(playerSuggestion.getProfile().getId());

				if (player != null) {
					if (player.isPartVisible(PlayerModelPart.HAT)) {
						DrawableHelper.drawTexture(matrices, this.area.getX() - 16, this.area.getY() + (12 * entry) + 2, 40.0F, 8.0F, 8, 8, 64, 64);
					}
				}
			}*/
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestion;getText()Ljava/lang/String;"), method = "render")
	private String cacheTextColor(Suggestion suggestion) {
		if (suggestion instanceof SuggestionWithData) {
			final ColorSuggestionData suggestionData = SuggestionData.get(SuggestionTypes.COLOR, suggestion);

			this.cacheColor = suggestionData.getColor().getRgb();
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
