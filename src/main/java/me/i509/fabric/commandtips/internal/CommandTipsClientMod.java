package me.i509.fabric.commandtips.internal;

import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.api.config.CommandTipsConfig;
import me.i509.fabric.commandtips.api.render.SuggestionRenderer;
import me.i509.fabric.commandtips.api.render.SuggestionRendererRegistry;
import me.i509.fabric.commandtips.api.suggestion.BlockSuggestionData;
import me.i509.fabric.commandtips.api.suggestion.ItemSuggestionData;
import me.i509.fabric.commandtips.api.suggestion.PlayerSuggestionData;
import me.i509.fabric.commandtips.api.suggestion.SuggestionTypes;
import org.checkerframework.checker.nullness.qual.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class CommandTipsClientMod implements ClientModInitializer {
	private static final CachedEntityTracker entityTracker = new CachedEntityTracker();
	private static final PlayerModelTracker playerModelTracker = new PlayerModelTracker();
	private static CommandTipsConfigImpl config = new CommandTipsConfigImpl();

	public static CachedEntityTracker getEntityTracker() {
		return entityTracker;
	}

	public static PlayerModelTracker getPlayerModelTracker() {
		return playerModelTracker;
	}

	public static CommandTipsConfig getConfig() {
		return config;
	}

	@Override
	public void onInitializeClient() {
		new EventSwitchboard();
		this.registerBuiltinSuggestionRenderers();
	}

	private void registerBuiltinSuggestionRenderers() {
		SuggestionRendererRegistry.register(SuggestionTypes.PLAYER, new PlayerSuggestionRenderer());
		SuggestionRendererRegistry.register(SuggestionTypes.ITEM, new ItemSuggestionRenderer());
		SuggestionRendererRegistry.register(SuggestionTypes.BLOCK, new BlockSuggestionRenderer());
	}

	@Environment(EnvType.CLIENT)
	private static final class PlayerSuggestionRenderer extends DrawableHelper implements SuggestionRenderer<PlayerSuggestionData> {
		@Override
		public void render(Suggestion suggestion, PlayerSuggestionData data, MatrixStack matrices, int suggestionEntry, int x, int y, int mouseX, int mouseY) {				// http://assets.mojang.com/SkinTemplates/4px_reference.png
			// Reference skin: http://assets.mojang.com/SkinTemplates/4px_reference.png
			final MinecraftClient client = MinecraftClient.getInstance();
			boolean hatModelPartVisible = false;

			if (client.player != null) {
				// We can check the client player if it is ourselves
				if (data.getListEntry().getProfile().equals(client.player.getGameProfile())) {
					hatModelPartVisible = client.player.isPartVisible(PlayerModelPart.HAT);
				} else {
					assert client.world != null;
					@Nullable final PlayerEntity player = client.world.getPlayerByUuid(data.getListEntry().getProfile().getId());

					if (player != null) {
						hatModelPartVisible = player.isPartVisible(PlayerModelPart.HAT);
					} else {
						if (getPlayerModelTracker().isTracked(data.getListEntry().getProfile().getId())) {
							hatModelPartVisible = getPlayerModelTracker().isHatVisible(data.getListEntry().getProfile().getId());
						} else {
							// We do not know any of the player's visible mod parts, assume the hat is visible.
							hatModelPartVisible = true;
						}
					}
				}
			}

			client.getTextureManager().bindTexture(data.getListEntry().getSkinTexture());
			drawTexture(matrices, x, y + 2, 8.0F, 8.0F, 8, 8, 64, 64);

			if (hatModelPartVisible) {
				drawTexture(matrices, x, y + 2, 40.0F, 8.0F, 8, 8, 64, 64);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	private static final class ItemSuggestionRenderer extends DrawableHelper implements SuggestionRenderer<ItemSuggestionData> {
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

	@Environment(EnvType.CLIENT)
	private static final class BlockSuggestionRenderer extends DrawableHelper implements SuggestionRenderer<BlockSuggestionData> {
		@Override
		public void render(Suggestion suggestion, BlockSuggestionData data, MatrixStack matrices, int suggestionEntry, int x, int y, int mouseX, int mouseY) {
			final MinecraftClient client = MinecraftClient.getInstance();

			client.getBlockRenderManager().renderBlockAsEntity(data.getBlock().getDefaultState(), matrices, client.getBufferBuilders().getEntityVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV);
		}
	}
}
