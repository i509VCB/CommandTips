package me.i509.fabric.commandtips.mixin.screen;

import java.util.List;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChatScreen.class)
public class ChatScreenMixin_Core extends Screen {
	protected ChatScreenMixin_Core(Text title) {
		super(title);
	}
}
