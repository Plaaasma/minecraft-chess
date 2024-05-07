package net.nerdorg.chess.screen;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.nerdorg.chess.ChessForMinecraft;
import net.nerdorg.chess.block.entity.ChessBlockEntity;
import net.nerdorg.chess.networking.ClientPacketHandler;

import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ChessScreen extends Screen {
    private final ChessBlockEntity chessBlockEntity;
    private BlockPos chessBoardPos;
    private boolean side;
    private Side playingSide;
    private boolean spectator = true;
    private Square selectedSquare = null;

    public ChessScreen(Text title, int x, int y, int z, boolean side) {
        super(title);

        this.client = MinecraftClient.getInstance();

        this.chessBoardPos = new BlockPos(x, y, z);
        this.chessBlockEntity = (ChessBlockEntity) this.client.world.getBlockEntity(this.chessBoardPos);
        this.side = side;
    }

    @Override
    protected void init() {
        super.init();
    }

    private Square getSquareFromScreenCoordinates(int screenX, int screenY) {
        int boardCornerX = this.width / 2 - 64;
        int boardCornerY = this.height / 2 - 64;

        // Translate screen coordinates to board-relative coordinates
        int boardX = screenX - boardCornerX;
        int boardY = screenY - boardCornerY;

        // Each square is 16x16 pixels
        if (boardX < 0 || boardX >= 128 || boardY < 0 || boardY >= 128) {
            // The coordinates are outside the chessboard
            return null;
        }

        int row, column;

        if (this.side) {
            // Top player (normal orientation)
            row = 8 - (boardY / 16);
            column = (boardX / 16) + 1;
        } else {
            // Bottom player (flipped orientation)
            row = (boardY / 16) + 1;
            column = 8 - (boardX / 16);
        }

        return Square.squareAt((row - 1) * 8 + (column - 1));
    }

    private void renderBoardImage(DrawContext context) {
        context.drawTexture(ChessTextures.BACKGROUND_TEXTURE, this.width / 2 - 80, this.height / 2 - 80, 0.0F, 0.0F, 160, 160, 160, 160);
        context.drawTexture(ChessTextures.BOARD_TEXTURE, this.width / 2 - 64, this.height / 2 - 64, 0.0F, 0.0F, 128, 128, 128, 128);
    }

    private void renderPieces(DrawContext context) {
        int boardCornerX = this.width / 2 - 64;
        int boardCornerY = this.height / 2 - 64;

        for (int square = 0; square < 64; square++) {
            Square squareAt = Square.squareAt(square);
            Piece pieceAt = this.chessBlockEntity.getBoard().getPiece(squareAt);

            if (pieceAt != Piece.NONE) {
                if (this.side) {
                    int row = 8 - (square / 8);
                    int column = (square % 8) + 1;

                    int xOffset = (column * 16);
                    int yOffset = (row * 16);

                    Identifier pieceTextureAt = ChessTextures.textureFromPiece(pieceAt);

                    context.drawTexture(pieceTextureAt, boardCornerX + xOffset - 16, boardCornerY + yOffset - 16, 0.0F, 0.0F, 16, 16, 16, 16);
                }
                else {
                    int row = (square / 8) + 1;
                    int column = 8 - (square % 8);

                    int xOffset = (column * 16);
                    int yOffset = (row * 16);

                    Identifier pieceTextureAt = ChessTextures.textureFromPiece(pieceAt);

                    context.drawTexture(pieceTextureAt, boardCornerX + xOffset - 16, boardCornerY + yOffset - 16, 0.0F, 0.0F, 16, 16, 16, 16);
                }
            }
        }
    }

    private void drawFilledCircle(DrawContext context, int centerX, int centerY, int radius, int color) {
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x * x + y * y <= radius * radius) {
                    context.fill(centerX + x - 1, centerY + y - 1, centerX + x + 1, centerY + y + 1, color);
                }
            }
        }
    }

    private void renderSelectedSquare(DrawContext context) {
        int boardCornerX = this.width / 2 - 64;
        int boardCornerY = this.height / 2 - 64;

        int squareIndex = this.selectedSquare.ordinal();
        int row, column;

        if (this.side) {
            // Top player (normal orientation)
            row = 8 - (squareIndex / 8);
            column = (squareIndex % 8) + 1;
        } else {
            // Bottom player (flipped orientation)
            row = (squareIndex / 8) + 1;
            column = 8 - (squareIndex % 8);
        }

        int xOffset = (column * 16);
        int yOffset = (row * 16);

        int x = boardCornerX + xOffset - 16;
        int y = boardCornerY + yOffset - 16;

        // Draw the highlight
        context.fill(x, y, x + 16, y + 16, 0x8070A7FF);
    }

    private void renderLegalMoves(DrawContext context) {
        int boardCornerX = this.width / 2 - 64;
        int boardCornerY = this.height / 2 - 64;

        List<Move> legalMoves = this.chessBlockEntity.getBoard().legalMoves();

        for (Move legalMove : legalMoves) {
            if (legalMove.getFrom() == this.selectedSquare) {
                Piece pieceAt = this.chessBlockEntity.getBoard().getPiece(legalMove.getTo());
                int squareIndex = legalMove.getTo().ordinal();
                int row, column;

                if (this.side) {
                    // Top player (normal orientation)
                    row = 8 - (squareIndex / 8);
                    column = (squareIndex % 8) + 1;
                } else {
                    // Bottom player (flipped orientation)
                    row = (squareIndex / 8) + 1;
                    column = 8 - (squareIndex % 8);
                }

                int xOffset = (column * 16);
                int yOffset = (row * 16);

                int x = boardCornerX + xOffset - 16;
                int y = boardCornerY + yOffset - 16;

                // Draw the highlight
                if (pieceAt == Piece.NONE) {
                    drawFilledCircle(context, x + 8, y + 8, 2, 0x8070A7FF);
                }
                else {
                    context.fill(x, y, x + 16, y + 16, 0x8070A7FF);
                }
            }
        }
    }

    private void renderCheck(DrawContext context) {
        if (this.chessBlockEntity.getBoard().isKingAttacked()) {
            Square kingSquare;

            if (this.chessBlockEntity.getBoard().getSideToMove() == Side.BLACK) {
                kingSquare = this.chessBlockEntity.getBoard().getKingSquare(Side.BLACK);
            }
            else {
                kingSquare = this.chessBlockEntity.getBoard().getKingSquare(Side.WHITE);
            }

            int boardCornerX = this.width / 2 - 64;
            int boardCornerY = this.height / 2 - 64;

            int squareIndex = kingSquare.ordinal();
            int row, column;

            if (this.side) {
                // Top player (normal orientation)
                row = 8 - (squareIndex / 8);
                column = (squareIndex % 8) + 1;
            } else {
                // Bottom player (flipped orientation)
                row = (squareIndex / 8) + 1;
                column = 8 - (squareIndex % 8);
            }

            int xOffset = (column * 16);
            int yOffset = (row * 16);

            int x = boardCornerX + xOffset - 16;
            int y = boardCornerY + yOffset - 16;

            // Draw the highlight
            context.fill(x, y, x + 16, y + 16, 0x80FF4545);
        }
    }

    private void renderHighlights(DrawContext context) {
        if (this.chessBlockEntity != null) {
            if (this.selectedSquare != null) {
                renderSelectedSquare(context);
                renderLegalMoves(context);
            }
            renderCheck(context);
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.chessBlockEntity != null) {
            this.spectator = this.chessBlockEntity.getWhitePlayer() == null || this.chessBlockEntity.getBlackPlayer() == null || !(this.chessBlockEntity.getWhitePlayer().equals(this.client.player.getUuidAsString()) || this.chessBlockEntity.getBlackPlayer().equals(this.client.player.getUuidAsString()));

            if (this.chessBlockEntity.getWhitePlayer() != null && this.chessBlockEntity.getWhitePlayer().equals(this.client.player.getUuidAsString())) {
                this.playingSide = Side.WHITE;
            }
            else if (this.chessBlockEntity.getBlackPlayer() != null && this.chessBlockEntity.getBlackPlayer().equals(this.client.player.getUuidAsString())) {
                this.playingSide = Side.BLACK;
            }

            renderBoardImage(context);
            renderHighlights(context);
            renderPieces(context);

            if (this.chessBlockEntity.getBoard().isMated()) {
                context.drawCenteredTextWithShadow(this.textRenderer, "Game Over! Checkmate!", this.width / 2, this.height / 2, Formatting.GOLD.getColorValue());
            }
            else if (this.chessBlockEntity.getBoard().isStaleMate()) {
                context.drawCenteredTextWithShadow(this.textRenderer, "Game Over! Draw by stalemate!", this.width / 2, this.height / 2, Formatting.GRAY.getColorValue());
            }
            else if (this.chessBlockEntity.getBoard().isRepetition()) {
                context.drawCenteredTextWithShadow(this.textRenderer, "Game Over! Draw by repetition!", this.width / 2, this.height / 2, Formatting.GRAY.getColorValue());
            }
            else if (this.chessBlockEntity.getBoard().isDraw()) {
                context.drawCenteredTextWithShadow(this.textRenderer, "Game Over! Draw!", this.width / 2, this.height / 2, Formatting.GRAY.getColorValue());
            }
        }
        else {
            this.close();
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.chessBlockEntity != null && !this.spectator) {
            if (!this.chessBlockEntity.getBoard().isMated() && !this.chessBlockEntity.getBoard().isDraw() && !this.chessBlockEntity.getBoard().isStaleMate()) {
                if (this.chessBlockEntity.getBoard().getSideToMove() == this.playingSide) {
                    Square square = getSquareFromScreenCoordinates((int) mouseX, (int) mouseY);
                    if (square != null) {
                        if (this.selectedSquare == null) {
                            Piece pieceAt = this.chessBlockEntity.getBoard().getPiece(square);
                            if (pieceAt != Piece.NONE && pieceAt.getPieceSide() == this.playingSide) {
                                this.selectedSquare = square;
                            }
                        } else {
                            List<Move> legalMoves = this.chessBlockEntity.getBoard().legalMoves();

                            boolean found_move = false;
                            for (Move legalMove : legalMoves) {
                                if (legalMove.getFrom() == this.selectedSquare && legalMove.getTo() == square) {
                                    Board boardCopy = this.chessBlockEntity.getBoard().clone();
                                    boardCopy.doMove(legalMove);
                                    ClientPacketHandler.pushMove(this.chessBoardPos, boardCopy);
                                    found_move = true;
                                    break;
                                }
                            }

                            Piece pieceAt = this.chessBlockEntity.getBoard().getPiece(square);

                            if (found_move || pieceAt == Piece.NONE || !(pieceAt.getPieceSide() == this.playingSide)) {
                                this.selectedSquare = null;
                            } else {
                                this.selectedSquare = square;
                            }
                        }
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
