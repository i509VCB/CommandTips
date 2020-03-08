package me.i509.fabric.commandtips.bridge;

import com.mojang.brigadier.arguments.ArgumentType;

public interface CommandSuggestorBridge {
	ArgumentType<?> getCurrentArgumentType();
}
