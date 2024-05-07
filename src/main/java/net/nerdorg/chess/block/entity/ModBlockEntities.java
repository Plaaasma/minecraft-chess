package net.nerdorg.chess.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nerdorg.chess.ChessForMinecraft;
import net.nerdorg.chess.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<ChessBlockEntity> CHESS_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ChessForMinecraft.MOD_ID, "chess_be"),
                    FabricBlockEntityTypeBuilder.create(ChessBlockEntity::new, ModBlocks.CHESS_BLOCK).build());

    public static void registerBlockEntities() {
        ChessForMinecraft.LOGGER.info("Registering Block Entities for " + ChessForMinecraft.MOD_ID);
    }
}
