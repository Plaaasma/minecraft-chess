package net.nerdorg.chess.networking;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.nerdorg.chess.block.entity.ChessBlockEntity;
import net.nerdorg.chess.util.BlockEntityUtil;

import java.util.HashMap;
import java.util.List;

public class PacketHandler {
    public static void sendOpenBoard(ServerPlayerEntity player, int x, int y, int z, boolean side) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(side);

        ServerPlayNetworking.send(player, ModMessages.OPEN_BOARD, buf);
    }

    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ModMessages.PUSH_MOVE, (server, player, handler, buf, responseSender) -> {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            String newFen = buf.readString();

            ChessBlockEntity.fenToPush.put(new BlockPos(x, y, z), newFen);
        });
    }
}
