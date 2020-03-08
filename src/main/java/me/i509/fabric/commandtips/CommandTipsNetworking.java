package me.i509.fabric.commandtips;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CommandTipsNetworking {
	/**
	 * Checks if this server understands the custom packets to ask for extra completions.
	 * @return True if the server understands, otherwise false.
	 */
	public boolean doesServerUnderstand() {
		return false;
	}
}
