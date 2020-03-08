package me.i509.fabric.commandtips;

import java.util.function.Function;
import me.i509.fabric.commandtips.api.event.ReceiveCommandTreeCallback;

public class ReceiveCommandTreeCallbackInternals {
	public static final Function<ReceiveCommandTreeCallback[], ReceiveCommandTreeCallback> EVENT_EXECUTOR = (callbacks -> serverTree -> {
		for (ReceiveCommandTreeCallback callback : callbacks) {
			callback.onReceive(serverTree);
		}
	});
}
