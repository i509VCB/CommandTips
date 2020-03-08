package me.i509.fabric.commandtips.mixin.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractCommandBlockScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(AbstractCommandBlockScreen.class)
public abstract class AbstractCommandBlockScreenMixin_Core extends Screen {
	protected AbstractCommandBlockScreenMixin_Core(Text title) {
		super(title);
	}
}
