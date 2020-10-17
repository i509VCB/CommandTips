package me.i509.fabric.commandtips.api.suggestion;

import net.minecraft.client.network.PlayerListEntry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class PlayerSuggestionData extends AbstractSuggestionData<PlayerSuggestionData> {
	private final PlayerListEntry entry;

	public PlayerSuggestionData(PlayerListEntry entry) {
		super(SuggestionTypes.PLAYER);
		this.entry = entry;
	}

	public PlayerListEntry getListEntry() {
		return this.entry;
	}
}
