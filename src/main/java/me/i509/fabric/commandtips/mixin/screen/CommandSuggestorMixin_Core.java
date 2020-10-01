package me.i509.fabric.commandtips.mixin.screen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import me.i509.fabric.commandtips.bridge.CommandSuggestorBridge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.server.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandSuggestor.class)
public abstract class CommandSuggestorMixin_Core implements CommandSuggestorBridge {
	@Shadow
	@Final
	private MinecraftClient client;
	@Shadow
	@Final
	private Screen owner;
	@Shadow
	@Final
	private TextFieldWidget textField;
	@Shadow
	@Final
	private TextRenderer textRenderer;
	@Shadow
	private CommandSuggestor.SuggestionWindow window;
	@Shadow
	private ParseResults<CommandSource> parse;
	@Shadow
	private CompletableFuture<Suggestions> pendingSuggestions;
	@Shadow
	private List<String> messages;

	// TODO
	// Get the current argument type that the cursor is at, check if it's the last argument
	// Mixin to Render

	public void junk() {
		ImmutableStringReader reader = this.parse.getReader();
		int cursorPosition = reader.getCursor();

		CommandContextBuilder<CommandSource> contextBuilder = this.parse.getContext();
		SuggestionContext<CommandSource> suggestionContext = contextBuilder.findSuggestionContext(cursorPosition);

		CommandNode<CommandSource> previousNode = suggestionContext.parent;

		if (!previousNode.getChildren().isEmpty()) {
			for (CommandNode<CommandSource> child : previousNode.getChildren()) {
				child.getRelevantNodes((StringReader) reader);
			}

			int currentNodeStart = suggestionContext.startPos;

			if (previousNode instanceof ArgumentCommandNode) {
				ArgumentCommandNode<CommandSource, ?> currentArgumentCommandNode = (ArgumentCommandNode<CommandSource, ?>) previousNode;
				ArgumentType<?> currentArgumentType = currentArgumentCommandNode.getType();
			}
		}
	}
}
