package net.diversity.locked.item;

import net.diversity.locked.Locked;
import net.diversity.locked.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup CUSTOM_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Locked.MOD_ID, "cell_bars"),
            FabricItemGroup.builder().displayName(Text.translatable("itemGroup." + Locked.MOD_ID + ".customBlocks"))
                    .icon(() -> new ItemStack(ModBlocks.CELL_BARS)).entries((displayContext, entries) -> {
                        entries.add(ModBlocks.CELL_BARS);
            }).build());

    public static void registerItemGroups() {
        Locked.LOGGER.info("Registering Item Groups for " + Locked.MOD_ID);
    }
}
