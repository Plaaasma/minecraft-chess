package net.nerdorg.chess.block.entity;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class ChessBlockEntity extends BlockEntity {
    public static HashMap<BlockPos, String> fenToPush = new HashMap<>();

    private String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private String whitePlayer = null;
    private String blackPlayer = null;
    private Board board = new Board();

    public ChessBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CHESS_BE, blockPos, blockState);
    }

    public ChessBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setFen(String fen) {
        this.fen = fen;
        this.board.loadFromFen(fen);
    }

    public String getFen() {
        return fen;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("fen", this.fen != null ? this.fen : "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        nbt.putString("white", this.whitePlayer != null ? this.whitePlayer : "");
        nbt.putString("black", this.blackPlayer != null ? this.blackPlayer : "");
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.fen = nbt.getString("fen");
        this.whitePlayer = nbt.getString("white");
        this.blackPlayer = nbt.getString("black");
        this.board.loadFromFen(this.fen);
        this.markDirty();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = createNbt();

        nbt.putString("fen", this.fen != null ? this.fen : "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        nbt.putString("white", this.whitePlayer != null ? this.whitePlayer : "");
        nbt.putString("black", this.blackPlayer != null ? this.blackPlayer : "");

        return nbt;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ChessBlockEntity be) {
        if (world instanceof ServerWorld) {
            if (fenToPush.containsKey(pos)) {
                String newFen = fenToPush.get(pos);
                be.setFen(newFen);
            }

            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
    }
}
