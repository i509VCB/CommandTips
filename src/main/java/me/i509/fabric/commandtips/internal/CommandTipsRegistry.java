package me.i509.fabric.commandtips.internal;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.options.KeyBinding;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

@Environment(EnvType.CLIENT)
public final class CommandTipsRegistry {
	public static final KeyBinding CACHE_ENTITY = KeyBindingHelper.registerKeyBinding(new KeyBinding("commandtips.cacheEntity", GLFW.GLFW_KEY_U, "commandtips"));

	public static void init() {
	}

	private CommandTipsRegistry() {
	}
}
