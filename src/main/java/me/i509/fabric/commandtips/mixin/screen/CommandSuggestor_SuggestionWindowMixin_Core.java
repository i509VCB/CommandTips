package me.i509.fabric.commandtips.mixin.screen;

import java.util.List;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import me.i509.fabric.commandtips.suggestion.ItemRenderableSuggestion;
import me.i509.fabric.commandtips.suggestion.PlayerEntrySuggestion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.Rect2i;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = CommandSuggestor.SuggestionWindow.class, priority = 1001)
public abstract class CommandSuggestor_SuggestionWindowMixin_Core {
	@Shadow private Rect2i area;
	@Shadow @Final private Suggestions suggestions;

	@Inject(at = @At("TAIL"), method = "render(II)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void commandTips_renderItems(int mouseX, int mouseY, CallbackInfo ci, int i) {
		List<Suggestion> suggestionList = this.suggestions.getList();

		for(int m = 0; m < i; ++m) {
			Suggestion suggestion = suggestionList.get(m);

			if (suggestion instanceof ItemRenderableSuggestion) {
				ItemStack stack = new ItemStack(((ItemRenderableSuggestion) suggestion).getItem());
				BakedModel model = MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, null, MinecraftClient.getInstance().player);
				// Scale the item down for screen render then scale it back up
				Vector3f scale = model.getTransformation().gui.scale;
				if (stack.getItem() instanceof BlockItem) {
					scale.scale(0.65F);
				} else {
					scale.scale(0.45F);
				}

				MinecraftClient.getInstance().getItemRenderer().renderGuiItem(stack, this.area.getX() - 16, this.area.getY() + (12 * m) - 1);

				if (stack.getItem() instanceof BlockItem) {
					scale.scale((1F/0.65F));
				} else {
					scale.scale((1F/0.45F));
				}
			} else if (suggestion instanceof PlayerEntrySuggestion) {
				PlayerEntrySuggestion playerSuggestion = ((PlayerEntrySuggestion) suggestion);
				// http://assets.mojang.com/SkinTemplates/4px_reference.png
				// Reference skin
				//RenderSystem.pushMatrix();
				MinecraftClient.getInstance().getTextureManager().bindTexture(playerSuggestion.getSkinTexture());
				DrawableHelper.blit(this.area.getX() - 16, this.area.getY() + (12 * m) + 2, 8.0F, 8.0F, 8, 8, 64, 64);
				// TODO - Get all enabled skin parts on the player
				PlayerEntity playerEntity = MinecraftClient.getInstance().world.getPlayerByUuid(playerSuggestion.getProfile().getId());
				if (playerEntity != null) {
					if (playerEntity.isPartVisible(PlayerModelPart.HAT)) {
						DrawableHelper.blit(this.area.getX() - 16, this.area.getY() + (12 * m) + 2, 40.0F, 8.0F, 8, 8, 64, 64);
					}
				}
				// We can only do so much if the player instance can't actually be found
				//RenderSystem.popMatrix();
			}
		}
	}
}
