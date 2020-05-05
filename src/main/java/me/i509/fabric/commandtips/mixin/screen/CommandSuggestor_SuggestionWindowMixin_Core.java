package me.i509.fabric.commandtips.mixin.screen;

import java.util.List;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import me.i509.fabric.commandtips.suggestion.ColoredSuggestion;
import me.i509.fabric.commandtips.suggestion.ItemRenderableSuggestion;
import me.i509.fabric.commandtips.suggestion.PlayerEntrySuggestion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.Rect2i;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = CommandSuggestor.SuggestionWindow.class, priority = 1001)
public abstract class CommandSuggestor_SuggestionWindowMixin_Core {
	@Shadow private Rect2i area;
	@Shadow @Final private Suggestions suggestions;
	@SuppressWarnings("ShadowTarget") @Shadow private CommandSuggestor field_21615;
	@Shadow private int inWindowIndex;

	private Formatting ctp_cacheColor;

	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;II)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void commandTips_renderItems(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci, int suggestionListSize) {
		List<Suggestion> suggestionList = this.suggestions.getList();

		for(int m = 0; m < suggestionListSize; ++m) {
			Suggestion suggestion = suggestionList.get(m + this.inWindowIndex);

			if (suggestion instanceof ItemRenderableSuggestion) {
				ItemStack stack = new ItemStack(((ItemRenderableSuggestion) suggestion).getItem());
				BakedModel model = MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, null, MinecraftClient.getInstance().player);
				// Scale the item down for screen render then scale it back up
				Vector3f scale = model.getTransformation().gui.scale;
				if (stack.getItem() instanceof SignItem) {
					scale.scale(0.55F);
				} else if (stack.getItem() instanceof BlockItem) {
					scale.scale(0.65F);
				} else {
					scale.scale(0.020F);
				}

				MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, this.area.getX() - 16, this.area.getY() + (12 * m) - 1);

				if (stack.getItem() instanceof SignItem) { // Special handling for the signs
					scale.scale((1/0.55F));
				} else if (stack.getItem() instanceof BlockItem) {
					scale.scale((1F/0.65F));
				} else {
					scale.scale((1F/0.020F));
				}
			} else if (suggestion instanceof PlayerEntrySuggestion) {
				PlayerEntrySuggestion playerSuggestion = ((PlayerEntrySuggestion) suggestion);
				// http://assets.mojang.com/SkinTemplates/4px_reference.png
				// Reference skin
				MinecraftClient.getInstance().getTextureManager().bindTexture(playerSuggestion.getSkinTexture());
				DrawableHelper.drawTexture(matrices, this.area.getX() - 16, this.area.getY() + (12 * m) + 2, 8.0F, 8.0F, 8, 8, 64, 64);
				// We can only do so much if the player instance can't actually be found, so we do our best to get the visible skin parts.
				PlayerEntity playerEntity = MinecraftClient.getInstance().world.getPlayerByUuid(playerSuggestion.getProfile().getId());
				if (playerEntity != null) {
					if (playerEntity.isPartVisible(PlayerModelPart.HAT)) {
						DrawableHelper.drawTexture(matrices, this.area.getX() - 16, this.area.getY() + (12 * m) + 2, 40.0F, 8.0F, 8, 8, 64, 64);
					}
				}
			}
		}
	}
	// Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I
	@Redirect(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/suggestion/Suggestion;getText()Ljava/lang/String;"), method = "render")
	private String ctp_cacheTextColor(Suggestion suggestion) {
		if (suggestion instanceof ColoredSuggestion) {
			this.ctp_cacheColor = ((ColoredSuggestion) suggestion).getFormatting();
		} else {
			this.ctp_cacheColor = null;
		}

		return suggestion.getText();
	}

	// Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;FFI)I
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"), method = "render", index = 4)
	private int ctp_setTextColor(int color) {
		if (this.ctp_cacheColor != null && color == -5592406) {
			return this.ctp_cacheColor.getColorValue();
		}

		return color;
	}
}
