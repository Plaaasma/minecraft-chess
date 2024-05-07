package net.nerdorg.chess.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nerdorg.chess.ChessForMinecraft;
import net.nerdorg.chess.block.ModBlocks;

public class ModItems {
    private static void addItemsToFunctionalTab(FabricItemGroupEntries entries) {
        entries.add(ModBlocks.CHESS_BLOCK.asItem());
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ChessForMinecraft.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ChessForMinecraft.LOGGER.info("Registering Mod Items for " + ChessForMinecraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::addItemsToFunctionalTab);
    }
}
