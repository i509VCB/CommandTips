package me.i509.fabric.commandtips.api.event;

import com.mojang.brigadier.CommandDispatcher;
import me.i509.fabric.commandtips.ReceiveCommandTreeCallbackInternals;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.command.CommandSource;

public interface ReceiveCommandTreeCallback {
	Event<ReceiveCommandTreeCallback> EVENT = EventFactory.createArrayBacked(ReceiveCommandTreeCallback.class, ReceiveCommandTreeCallbackInternals.EVENT_EXECUTOR);

	void onReceive(CommandDispatcher<CommandSource> serverTree);
}
