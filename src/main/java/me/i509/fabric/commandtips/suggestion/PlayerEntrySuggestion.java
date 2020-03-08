package me.i509.fabric.commandtips.suggestion;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

public class PlayerEntrySuggestion extends Suggestion {
	private final PlayerListEntry player;
	private final GameProfile profile;
	private final Identifier skinTexture;

	public PlayerEntrySuggestion(StringRange range, String text, PlayerListEntry player, GameProfile profile, Identifier skinTexture) {
		super(range, text);
		this.player = player;
		this.profile = profile;
		this.skinTexture = skinTexture;
	}

	public Identifier getSkinTexture() {
		return this.skinTexture;
	}

	public GameProfile getProfile() {
		return this.profile;
	}

	public PlayerListEntry getPlayer() {
		return player;
	}
}
