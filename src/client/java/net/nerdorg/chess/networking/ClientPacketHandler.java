package net.nerdorg.chess.networking;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.move.Move;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.nerdorg.chess.block.entity.ChessBlockEntity;
import net.nerdorg.chess.screen.ChessScreen;

public class ClientPacketHandler {
    public static void pushMove(BlockPos chessBoardPos, Board board) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(chessBoardPos.getX());
        buf.writeInt(chessBoardPos.getY());
        buf.writeInt(chessBoardPos.getZ());
        buf.writeString(board.getFen());

        ClientPlayNetworking.send(ModMessages.PUSH_MOVE, buf);
    }

    public static void registerReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(ModMessages.OPEN_BOARD, (client, handler, buf, responseSender) -> {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            boolean side = buf.readBoolean();

            client.execute(() -> {
                client.setScreen(new ChessScreen(Text.literal("Chess Board"), x, y, z, side));
            });
        });
    }
}
