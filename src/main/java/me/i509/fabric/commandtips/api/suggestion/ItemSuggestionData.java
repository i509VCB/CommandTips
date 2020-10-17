package me.i509.fabric.commandtips.api.suggestion;

import net.minecraft.item.Item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ItemSuggestionData extends AbstractSuggestionData<ItemSuggestionData> {
	private final Item item;

	public ItemSuggestionData(Item item) {
		super(SuggestionTypes.ITEM);
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}
}
