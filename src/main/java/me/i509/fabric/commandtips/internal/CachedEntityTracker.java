package me.i509.fabric.commandtips.internal;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class CachedEntityTracker {
	private UUID uuid;
	private EntityType<?> type;

	CachedEntityTracker() {
	}

	void startTrackingCursorEntity(UUID uuid, EntityType<?> type) {
		this.uuid = uuid;
		this.type = type;
	}

	void stopTrackingCursorEntity(Entity entity) {
		if (entity.getUuid().equals(this.uuid)) {
			this.uuid = null;
			this.type = null;
		}
	}

	public EntityType<?> getType() {
		return this.type;
	}

	public UUID getUuid() {
		return this.uuid;
	}
}
