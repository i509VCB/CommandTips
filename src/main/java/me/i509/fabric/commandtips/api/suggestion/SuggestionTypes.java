package me.i509.fabric.commandtips.api.suggestion;

/**
 * An enumeration of all built-in suggestion types.
 */
public final class SuggestionTypes {
	public static final SuggestionType<ItemSuggestionData> ITEM = SuggestionType.of();
	public static final SuggestionType<BlockSuggestionData> BLOCK = SuggestionType.of();
	public static final SuggestionType<PlayerSuggestionData> PLAYER = SuggestionType.of();
	public static final SuggestionType<ColorSuggestionData> COLOR = SuggestionType.of();

	private SuggestionTypes() {
	}
}
