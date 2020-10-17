package me.i509.fabric.commandtips.api.suggestion;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
abstract /* sealed */ class AbstractSuggestionData<D extends AbstractSuggestionData<D>> implements SuggestionData<D> {
	private final SuggestionType<D> type;

	protected AbstractSuggestionData(SuggestionType<D> type) {
		this.type = type;
	}

	@Override
	public SuggestionType<D> getType() {
		return this.type;
	}
}
