package me.i509.fabric.commandtips.mixin.command.argument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.i509.fabric.commandtips.CommandTipsClient;
import me.i509.fabric.commandtips.suggestion.ColoredSuggestion;
import me.i509.fabric.commandtips.suggestion.PlayerEntrySuggestion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityArgumentType.class)
public abstract class EntityArgumentTypeMixin_Suggestions {
	@Shadow @Final private boolean playersOnly;

	@Inject(at = @At(value = "RETURN"), method = "listSuggestions")
	private void commandTips_addCachedSuggestion(CommandContext<CommandSource> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
		if (context.getSource() instanceof ServerCommandSource) {
			return;
		}

		cir.getReturnValue().thenApply(suggestions ->  {
			final CommandTipsClient client = CommandTipsClient.getInstance();
			final StringRange range = suggestions.getRange();
			final List<Suggestion> oldSuggestionList = suggestions.getList();
			final List<Suggestion> suggestionList = new ArrayList<>();

			ColoredSuggestion colorSuggestion = null;

			// TODO: Check if a UUID can actually be displayed with the current suggestions or not, if not then don't add it.
			// TODO: Add a tooltip so we can display the entity type being stored.
			if (client.getCachedEntityType().isPresent() && client.getCachedEntityUUID().isPresent()) {
				if (!this.playersOnly || client.getCachedEntityType().get().equals(EntityType.PLAYER)) {
					colorSuggestion = new ColoredSuggestion(range, client.getCachedEntityUUID().get().toString(), client.getConfig().cachedEntitySelectorColor);
					suggestionList.add(colorSuggestion);
				}
			}

			// Now display the face of the player next to each player on the list known to the client

			for (final Suggestion suggestion : oldSuggestionList) {
				final Collection<PlayerListEntry> players = MinecraftClient.getInstance().getNetworkHandler().getPlayerList();

				for (final PlayerListEntry player : players) {
					if (player.getProfile().getName().equals(suggestion.getText())) {
						suggestionList.add(new PlayerEntrySuggestion(range, suggestion.getText(), player, player.getProfile(), player.getSkinTexture()));
						break;
					}

					suggestionList.add(suggestion);
				}
			}

			return new Suggestions(range, suggestionList);
		});
	}
}
