package me.i509.fabric.commandtips.mixin.command.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.i509.fabric.commandtips.suggestion.ItemRenderableSuggestion;
import net.minecraft.command.arguments.BlockArgumentParser;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockArgumentParser.class)
public abstract class BlockArgumentParserMixin_Suggestions implements ArgumentType<BlockStateArgument> {
	@Inject(at = @At("TAIL"), method = "getSuggestions", cancellable = true)
	private void commandTips_BlockPreviewSuggestion(SuggestionsBuilder builder, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
		CompletableFuture<Suggestions> future = cir.getReturnValue();
		cir.setReturnValue(future.thenApply(suggestions -> {
			List<Suggestion> modifiedSuggestions = new ArrayList<>();

			for (Suggestion suggestion : suggestions.getList()) {
				Optional<Item> item = Registry.ITEM.getOrEmpty(Identifier.tryParse(suggestion.getText()));

				if (!item.isPresent()) {
					modifiedSuggestions.add(suggestion);
					continue;
				}

				if (suggestion.getTooltip() != null) {
					modifiedSuggestions.add(new ItemRenderableSuggestion(suggestion.getRange(), suggestion.getText(), suggestion.getTooltip(), item.get()));
					continue;
				}

				modifiedSuggestions.add(new ItemRenderableSuggestion(suggestion.getRange(), suggestion.getText(), item.get()));
			}

			return new Suggestions(suggestions.getRange(), modifiedSuggestions);
		}));
	}
}
