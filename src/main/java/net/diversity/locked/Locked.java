package net.diversity.locked;

import net.diversity.locked.block.ModBlocks;
import net.diversity.locked.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Locked implements ModInitializer {

	public static final String MOD_ID = "locked";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	// Initialize the mod
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ServerPlayConnectionEvents.JOIN.register(Locked::onPlayerJoined);
	}

	public static void onPlayerJoined(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server)
	{
		server.execute(() -> {
			System.out.println(handler.player.getName() + "joined the fun");
		});

	}


}