package net.nerdorg.chess;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.nerdorg.chess.block.ModBlocks;
import net.nerdorg.chess.networking.ClientPacketHandler;

public class ChessForMinecraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPacketHandler.registerReceivers();
	}
}