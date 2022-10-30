package me.carlettos.chunkdata;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.carlettos.chunkdata.world.CustomDataManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ChunkData implements ModInitializer {
    public static final String MOD_ID = "chunkdata";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Random RNG = new Random();

	@Override
	public void onInitialize() {
	    ServerChunkEvents.CHUNK_UNLOAD.register(CustomDataManager::removeChunk);
	    Registry.register(Registry.ITEM, new Identifier(MOD_ID, "nbtctor"), new NBTctorItem());
	}
}
