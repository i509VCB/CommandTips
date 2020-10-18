package me.i509.fabric.commandtips.mixin.suggestion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.i509.fabric.commandtips.api.suggestion.ColorSuggestionData;
import me.i509.fabric.commandtips.api.suggestion.PlayerSuggestionData;
import me.i509.fabric.commandtips.internal.CachedEntityTracker;
import me.i509.fabric.commandtips.internal.CommandTipsClientMod;
import me.i509.fabric.commandtips.internal.SuggestionWithData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(EntityArgumentType.class)
abstract class EntityArgumentTypeMixin {
	@Shadow @Final private boolean playersOnly;

	@Inject(at = @At(value = "RETURN", ordinal = 0), method = "listSuggestions", cancellable = true)
	private void createRichEntitySuggestions(CommandContext<CommandSource> context, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
		// Do not add additional suggestions to server
		if (context.getSource() instanceof ServerCommandSource) {
			return;
		}

		cir.setReturnValue(cir.getReturnValue().thenApply(suggestions -> {
			final CachedEntityTracker tracker = CommandTipsClientMod.getEntityTracker();
			final StringRange range = suggestions.getRange();
			final List<Suggestion> oldSuggestionList = suggestions.getList();
			final List<Suggestion> suggestionList = new ArrayList<>();

			// TODO: Check if a UUID can actually be displayed with the current suggestions or not, if not then don't add it.
			// TODO: Add a tooltip so we can display the entity type being stored.
			// FIXME: Does not work
			if (tracker.getType() != null) {
				if (!this.playersOnly || tracker.getType().equals(EntityType.PLAYER)) {
					suggestionList.add(new SuggestionWithData(range, tracker.getUuid().toString(), new ColorSuggestionData(CommandTipsClientMod.getConfig().getCachedEntitySuggestionColor())));
				}
			}

			// Now display the face of the player next to each player on the list known to the client
			for (final Suggestion suggestion : oldSuggestionList) {
				final Collection<PlayerListEntry> players = MinecraftClient.getInstance().getNetworkHandler().getPlayerList();

				// FIXME: Brittle check to use username?
				for (final PlayerListEntry entry : players) {
					if (entry.getProfile().getName().equals(suggestion.getText())) {
						suggestionList.add(new SuggestionWithData(range, suggestion.getText(), new PlayerSuggestionData(entry)));
						break;
					}

					suggestionList.add(suggestion);
				}
			}

			return new Suggestions(range, suggestionList);
		}));
	}
}
