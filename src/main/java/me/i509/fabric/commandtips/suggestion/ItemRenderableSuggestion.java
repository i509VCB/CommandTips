package me.i509.fabric.commandtips.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.item.Item;

public class ItemRenderableSuggestion extends Suggestion {
	private Item item;

	public ItemRenderableSuggestion(StringRange range, String text, Item item) {
		super(range, text);
		this.item = item;
	}

	public ItemRenderableSuggestion(StringRange range, String text, Message tooltip, Item item) {
		super(range, text, tooltip);
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}
}
