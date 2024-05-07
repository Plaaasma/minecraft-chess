package net.nerdorg.chess;

import net.fabricmc.api.ModInitializer;

import net.nerdorg.chess.block.ModBlocks;
import net.nerdorg.chess.block.entity.ModBlockEntities;
import net.nerdorg.chess.commands.CommandRegister;
import net.nerdorg.chess.item.ModItems;
import net.nerdorg.chess.networking.PacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChessForMinecraft implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "chess";
    public static final Logger LOGGER = LoggerFactory.getLogger("chess");

	@Override
	public void onInitialize() {
		CommandRegister.register();

		PacketHandler.registerReceivers();

		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModBlockEntities.registerBlockEntities();
	}
}