package net.diversity.locked.block;

import net.diversity.locked.Locked;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    // Register custom blocks here
    public static final Block CELL_BARS = registerBlock("cell_bars", new CellBarsBlock(AbstractBlock.Settings.copy(Blocks.IRON_BARS)));



    // Register blocks methods
    public static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Locked.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(Locked.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Locked.LOGGER.info("Registering ModBlocks for " + Locked.MOD_ID);
    }

}
