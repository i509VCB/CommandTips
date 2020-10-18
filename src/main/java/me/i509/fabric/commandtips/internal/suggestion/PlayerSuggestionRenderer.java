package me.i509.fabric.commandtips.internal.suggestion;

import com.mojang.brigadier.suggestion.Suggestion;
import me.i509.fabric.commandtips.api.render.SuggestionRenderer;
import me.i509.fabric.commandtips.api.suggestion.PlayerSuggestionData;
import me.i509.fabric.commandtips.internal.CommandTipsClientMod;
import org.checkerframework.checker.nullness.qual.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class PlayerSuggestionRenderer extends DrawableHelper implements SuggestionRenderer<PlayerSuggestionData> {
	@Override
	public void render(Suggestion suggestion, PlayerSuggestionData data, MatrixStack matrices, int suggestionEntry, int x, int y, int mouseX, int mouseY) {                // http://assets.mojang.com/SkinTemplates/4px_reference.png
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
					if (CommandTipsClientMod.getPlayerModelTracker().isTracked(data.getListEntry().getProfile().getId())) {
						hatModelPartVisible = CommandTipsClientMod.getPlayerModelTracker().isHatVisible(data.getListEntry().getProfile().getId());
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
