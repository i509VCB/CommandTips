package me.i509.fabric.commandtips.internal.suggestion;

import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.api.render.SuggestionRenderer;
import me.i509.fabric.commandtips.api.suggestion.ItemSuggestionData;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ItemSuggestionRenderer extends DrawableHelper implements SuggestionRenderer<ItemSuggestionData> {
	@Override
	public void render(Suggestion suggestion, ItemSuggestionData data, MatrixStack matrices, int suggestionEntry, int x, int y, int mouseX, int mouseY) {
		final ItemStack stack = data.getItem().getDefaultStack();
		final MinecraftClient client = MinecraftClient.getInstance();
		final BakedModel model = client.getItemRenderer().getHeldItemModel(stack, null, client.player);

		// Scale the item down for screen render then scale it back up
		final Vector3f scale = model.getTransformation().gui.scale;

		if (data.getItem() instanceof BlockItem) {
			scale.scale(0.65F);
		} else {
			scale.scale(0.2F);
		}

		client.getItemRenderer().renderInGui(stack, x, y - 1);

		if (data.getItem() instanceof BlockItem) {
			scale.scale(1 / 0.65F);
		} else {
			scale.scale(1 / 0.2F);
		}
	}
}
