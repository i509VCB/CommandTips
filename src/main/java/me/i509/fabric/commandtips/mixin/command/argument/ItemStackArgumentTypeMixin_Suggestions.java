package me.i509.fabric.commandtips.mixin.command.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.i509.fabric.commandtips.api.suggestion.ItemSuggestionData;
import me.i509.fabric.commandtips.internal.SuggestionWithData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ItemStackArgumentType.class)
abstract class ItemStackArgumentTypeMixin_Suggestions {
	@Inject(at = @At("TAIL"), method = "listSuggestions", cancellable = true)
	private void createRichItemSuggestions(CommandContext<CommandSource> ctx, SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
		// Do not add additional suggestions to server
		if (ctx.getSource() instanceof ServerCommandSource) {
			return;
		}

		cir.getReturnValue().thenApply(suggestions -> {
			List<Suggestion> modifiedSuggestions = new ArrayList<>();

			for (Suggestion suggestion : suggestions.getList()) {
				Optional<Item> item = Registry.ITEM.getOrEmpty(Identifier.tryParse(suggestion.getText()));

				if (!item.isPresent()) {
					modifiedSuggestions.add(suggestion);
					continue;
				}

				if (suggestion.getTooltip() != null) {
					modifiedSuggestions.add(new SuggestionWithData(suggestion.getRange(), suggestion.getText(), suggestion.getTooltip(), new ItemSuggestionData(item.get())));
					continue;
				}

				modifiedSuggestions.add(new SuggestionWithData(suggestion.getRange(), suggestion.getText(), new ItemSuggestionData(item.get())));
			}

			return new Suggestions(suggestions.getRange(), modifiedSuggestions);
		});
	}
}
