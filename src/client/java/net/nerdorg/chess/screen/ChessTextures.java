package net.nerdorg.chess.screen;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import net.minecraft.util.Identifier;
import net.nerdorg.chess.ChessForMinecraft;

public class ChessTextures {
    public static final Identifier BACKGROUND_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/oak_background.png");
    public static final Identifier BOARD_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/chess_board.png");
    public static final Identifier WHITE_PAWN_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/wp.png");
    public static final Identifier WHITE_KNIGHT_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/wn.png");
    public static final Identifier WHITE_BISHOP_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/wb.png");
    public static final Identifier WHITE_ROOK_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/wr.png");
    public static final Identifier WHITE_QUEEN_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/wq.png");
    public static final Identifier WHITE_KING_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/wk.png");
    public static final Identifier BLACK_PAWN_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/bp.png");
    public static final Identifier BLACK_KNIGHT_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/bn.png");
    public static final Identifier BLACK_BISHOP_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/bb.png");
    public static final Identifier BLACK_ROOK_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/br.png");
    public static final Identifier BLACK_QUEEN_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/bq.png");
    public static final Identifier BLACK_KING_TEXTURE = new Identifier(ChessForMinecraft.MOD_ID, "textures/gui/bk.png");

    public static Identifier textureFromPiece(Piece piece) {
        if (piece.getPieceType() == PieceType.PAWN) {
            if (piece.getPieceSide() == Side.WHITE) {
                return WHITE_PAWN_TEXTURE;
            }
            else {
                return BLACK_PAWN_TEXTURE;
            }
        }
        else if (piece.getPieceType() == PieceType.KNIGHT) {
            if (piece.getPieceSide() == Side.WHITE) {
                return WHITE_KNIGHT_TEXTURE;
            }
            else {
                return BLACK_KNIGHT_TEXTURE;
            }
        }
        else if (piece.getPieceType() == PieceType.BISHOP) {
            if (piece.getPieceSide() == Side.WHITE) {
                return WHITE_BISHOP_TEXTURE;
            }
            else {
                return BLACK_BISHOP_TEXTURE;
            }
        }
        else if (piece.getPieceType() == PieceType.ROOK) {
            if (piece.getPieceSide() == Side.WHITE) {
                return WHITE_ROOK_TEXTURE;
            }
            else {
                return BLACK_ROOK_TEXTURE;
            }
        }
        else if (piece.getPieceType() == PieceType.KING) {
            if (piece.getPieceSide() == Side.WHITE) {
                return WHITE_KING_TEXTURE;
            }
            else {
                return BLACK_KING_TEXTURE;
            }
        }
        else {
            if (piece.getPieceSide() == Side.WHITE) {
                return WHITE_QUEEN_TEXTURE;
            }
            else {
                return BLACK_QUEEN_TEXTURE;
            }
        }
    }
}
