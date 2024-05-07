package net.nerdorg.chess.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;

public class BlockEntityUtil {
    public static <T extends BlockEntity, C extends BlockEntity> BlockEntityTicker<C> checkType(BlockEntityType<C> givenType, BlockEntityType<T> requiredType, BlockEntityTicker<? super T> ticker) {
        return givenType == requiredType ? (BlockEntityTicker<C>) ticker : null;
    }
}
