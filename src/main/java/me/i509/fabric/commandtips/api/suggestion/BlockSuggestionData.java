package me.i509.fabric.commandtips.api.suggestion;

import java.util.Optional;

import net.minecraft.block.Block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class BlockSuggestionData extends AbstractSuggestionData<BlockSuggestionData> {
	private final Block block;

	public BlockSuggestionData(Block block) {
		super(SuggestionTypes.BLOCK);
		this.block = block;
	}

	public Block getBlock() {
		return this.block;
	}

	public Optional<ItemSuggestionData> toItemData() {
		return Optional.ofNullable(this.block.asItem())
				.map(ItemSuggestionData::new);
	}
}
