package me.i509.fabric.commandtips.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class PlayerModelTracker {
	private final Map<UUID, Boolean> trackedPlayers = new HashMap<>();

	PlayerModelTracker() {
	}

	boolean isTracked(UUID uuid) {
		return this.trackedPlayers.containsKey(uuid);
	}

	boolean isHatVisible(UUID uuid) {
		return this.trackedPlayers.get(uuid);
	}

	void startTrackingPlayer(PlayerEntity entity) {
		this.trackedPlayers.put(entity.getUuid(), entity.isPartVisible(PlayerModelPart.HAT));
	}

	public void stopTrackingPlayer(UUID uuid) {
		this.trackedPlayers.remove(uuid);
	}
}
