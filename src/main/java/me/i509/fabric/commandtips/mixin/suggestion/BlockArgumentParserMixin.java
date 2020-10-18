package me.i509.fabric.commandtips.mixin.suggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.i509.fabric.commandtips.api.suggestion.BlockSuggestionData;
import me.i509.fabric.commandtips.internal.SuggestionWithData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.tag.TagGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(BlockArgumentParser.class)
abstract class BlockArgumentParserMixin implements ArgumentType<BlockStateArgument> {
	@Inject(at = @At("TAIL"), method = "getSuggestions", cancellable = true)
	private void createRichBlockSuggestions(SuggestionsBuilder builder, TagGroup<Block> group, CallbackInfoReturnable<CompletableFuture<Suggestions>> cir) {
		cir.setReturnValue(cir.getReturnValue().thenApply(suggestions -> {
			final List<Suggestion> modifiedSuggestions = new ArrayList<>();

			for (final Suggestion suggestion : suggestions.getList()) {
				final Optional<Block> block = Registry.BLOCK.getOrEmpty(Identifier.tryParse(suggestion.getText()));

				if (block.isPresent() && block.get().asItem() != null) {
					if (suggestion.getTooltip() != null) {
						modifiedSuggestions.add(new SuggestionWithData(suggestion.getRange(), suggestion.getText(), suggestion.getTooltip(), new BlockSuggestionData(block.get())));
					} else {
						modifiedSuggestions.add(new SuggestionWithData(suggestion.getRange(), suggestion.getText(), new BlockSuggestionData(block.get())));
					}
				}

				modifiedSuggestions.add(suggestion);
			}

			return new Suggestions(suggestions.getRange(), modifiedSuggestions);
		}));
	}
}
