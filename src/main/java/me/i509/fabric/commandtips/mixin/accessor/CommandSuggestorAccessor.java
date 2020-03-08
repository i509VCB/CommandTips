package me.i509.fabric.commandtips.mixin.accessor;

import com.mojang.brigadier.ParseResults;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.server.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommandSuggestor.class)
public interface CommandSuggestorAccessor {
	@Accessor("parse") ParseResults<CommandSource> accessor$getParseResults();
}
