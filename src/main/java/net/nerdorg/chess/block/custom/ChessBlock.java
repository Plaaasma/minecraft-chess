package net.nerdorg.chess.block.custom;

import com.github.bhlangonijr.chesslib.Board;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.nerdorg.chess.ChessForMinecraft;
import net.nerdorg.chess.block.entity.ChessBlockEntity;
import net.nerdorg.chess.block.entity.ModBlockEntities;
import net.nerdorg.chess.networking.PacketHandler;
import net.nerdorg.chess.util.BlockEntityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ChessBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING;

    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.125f, 0f, 0.125f, 0.875f, 0.88125f, 0.875f);

    public ChessBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world instanceof ServerWorld serverWorld) {
            ChessBlockEntity chessBlockEntity = (ChessBlockEntity) serverWorld.getBlockEntity(pos);

            if (chessBlockEntity != null) {
                boolean side = true;

                if ((chessBlockEntity.getWhitePlayer() == null || chessBlockEntity.getWhitePlayer().isEmpty()) && (chessBlockEntity.getBlackPlayer() == null || chessBlockEntity.getBlackPlayer().isEmpty())) {
                    Random random = new Random();
                    if (random.nextDouble() < 0.5) {
                        chessBlockEntity.setWhitePlayer(player.getUuidAsString());
                    }
                    else {
                        side = false;
                        chessBlockEntity.setBlackPlayer(player.getUuidAsString());
                    }
                }
                else if ((chessBlockEntity.getBlackPlayer() == null || chessBlockEntity.getBlackPlayer().isEmpty()) && !chessBlockEntity.getWhitePlayer().equals(player.getUuidAsString())) {
                    side = false;
                    chessBlockEntity.setBlackPlayer(player.getUuidAsString());
                }
                else {
                    if ((chessBlockEntity.getBlackPlayer() == null || chessBlockEntity.getBlackPlayer().isEmpty()) || !chessBlockEntity.getBlackPlayer().equals(player.getUuidAsString())) {
                        chessBlockEntity.setWhitePlayer(player.getUuidAsString());
                    }
                    else {
                        side = false;
                    }
                }

                PacketHandler.sendOpenBoard((ServerPlayerEntity) player, pos.getX(), pos.getY(), pos.getZ(), side);
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChessBlockEntity(ModBlockEntities.CHESS_BE, pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockEntityUtil.checkType(type, ModBlockEntities.CHESS_BE, (world1, pos, state1, be) -> ChessBlockEntity.tick(world1, pos, state1, be));
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
    }
}
