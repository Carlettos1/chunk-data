package me.carlettos.chunkdata.world;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.carlettos.chunkdata.mixins.ChunkSerializerMixin;
import me.carlettos.chunkdata.registry.CustomDataRegistry;
import me.carlettos.chunkdata.registry.DataType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;

/**
 * A manager of all the {@link CustomDataWorld} and {@link CustomDataChunk}, it is used to access all
 * custom data, but needs a {@link DataType} that can serialize it.
 * 
 * @see CustomDataRegistry
 */
public class CustomDataManager {
    private CustomDataManager() {}
    private static final ConcurrentHashMap<DimensionType, CustomDataWorld> DIMS = new ConcurrentHashMap<>();
    public static final Logger LOGGER = LoggerFactory.getLogger(CustomDataManager.class);
    
    /**
     * Returns the {@link CustomDataWorld} of the given {@link DimensionType}, if it not exists in
     * {@link CustomDataManager#DIMS}, then it creates an empty one.
     */
    public static final CustomDataWorld getCustomDataWorld(DimensionType dim) {
        if (!hasCustomDataWorld(dim)) {
            createCustomDataWorld(dim);
        }
        return DIMS.get(dim);
    }
    
    /**
     * Returns whenever {@link CustomDataManager#DIMS} contains a {@link CustomDataWorld}
     * with the given {@link DimensionType}.
     */
    private static final boolean hasCustomDataWorld(DimensionType dim) {
        return DIMS.containsKey(dim);
    }
    
    /**
     * Internal method. It creates a new empty {@link CustomDataWorld}.
     */
    private static final void createCustomDataWorld(DimensionType dim) {
        DIMS.put(dim, new CustomDataWorld());
        LOGGER.info("Created CustomDataWorld for dim " + dim.effects());
    }
    
    /**
     * This method shouldn't be used, as the data may not be saved to the chunk's NBT, because the
     * {@link ChunkSerializer#serialize(ServerWorld, Chunk)} method may not be called.
     * 
     * It's used by {@link ChunkSerializerMixin#deserialize} method to set the readed data and not
     * force the chunk to be unnecessary serialized.
     */
    @Deprecated
    public static final <T> void setDataNoSave(DimensionType dim, ChunkPos chunkPos, Identifier id, T data) {
        getCustomDataWorld(dim).setData(id, data, chunkPos);
    }
    
    /**
     * Changes the data that exists in the given chunk, in the given dimension. 
     * It needs the id of the registered {@link DataType}, that can serialize the data.
     * Also, tells the chunk that needs to be saved.
     * 
     * @param dim {@link DimensionType} of the chunk
     * @param chunk {@link WorldChunk} which its custom data will be modified
     * @param id {@link Identifier} of the serializer which will be used to serialize the data
     * @param data custom data to add to the chunk.
     * @param <T> Class of the data
     */
    public static final <T> void setData(DimensionType dim, Chunk chunk, Identifier id, T data) {
        notify(chunk);
        getCustomDataWorld(dim).setData(id, data, chunk.getPos());
    }
    
    public static final void notify(Chunk chunk) {
        chunk.setNeedsSaving(true);
    }

    /**
     * Changes the data that exists in the chunk on the given position, in the given world. 
     * It needs the id of the registered {@link DataType}, that can serialize the data.
     * Also, tells the chunk that needs to be saved.
     * 
     * @param world {@link ServerWorld} of the chunk
     * @param pos {@link BlockPos} of the entity/block which will be translated into a {@link ChunkPos}
     * @param id {@link Identifier} of the serializer which will be used to serialize the data
     * @param data custom data to add to the chunk
     * @param <T> Class of the data
     */
    public static final <T> void setData(World world, BlockPos pos, Identifier id, T data) {
        setData(world.getDimension(), world.getChunk(pos), id, data);
    }
    
    /**
     * Gets the data of the chunk in the given {@link ChunkPos}, in the given {@link World}.
     * It needs the id of the registered {@link DataType}, that can serialize the data.
     * 
     * @param world {@link ServerWorld} of the chunk
     * @param chunkPos {@link ChunkPos} of the chunk
     * @param id {@link Identifier} of the serializer
     * @param <T> Class of the data which will be retrieved
     * 
     * @return the data asociated to the given id
     */
    public static final <T> T getData(World world, ChunkPos chunkPos, Identifier id) {
        if (!getCustomDataWorld(world.getDimension()).hasChunk(chunkPos) && world.getChunk(chunkPos.getStartPos()).getStatus().equals(ChunkStatus.FULL)) {
            generateData(world.getDimension(), chunkPos);
            System.out.println("Data not found, generating for: " + chunkPos);
        }
        return getCustomDataWorld(world.getDimension()).getData(id, chunkPos);
    }
    
    /**
     * Gets the data of the given {@link WorldChunk}
     * 
     * @param chunk {@link WorldChunk} chunk
     * @param <T> Class of the data which will be retrieved
     * 
     * @return the data asociated to the given id
     */
    public static final <T> T getData(WorldChunk chunk, Identifier id) {
        return getData(chunk.getWorld(), chunk.getPos(), id);
    }
    
    /**
     * Returns whenever the {@link CustomDataManager#DIMS} contains the chunk in the given {@link ChunkPos},
     * in the given {@link DimensionType}.
     * 
     * @return <code>true</code> if the {@link ConcurrentHashMap} has the chunk in the given position.
     */
    public static final boolean hasChunk(DimensionType dim, ChunkPos chunkPos) {
        return getCustomDataWorld(dim).hasChunk(chunkPos);
    }
    
    /**
     * Generates the custom data of all the {@link DataType} registered in {@link CustomDataRegistry}
     * of the {@link WorldChunk} in the given {@link ChunkPos}, in the given {@link DimensionType}. It
     * uses {@link DataType#generate(ChunkPos)} to generate the data.
     */
    public static final void generateData(DimensionType dim, ChunkPos chunkPos) {
        CustomDataManager.getCustomDataWorld(dim).setChunk(chunkPos, new CustomDataChunk(chunkPos));
        CustomDataRegistry.forEach((id, dataType) -> {
            CustomDataManager.setDataNoSave(dim, chunkPos, id, dataType.generate(chunkPos));
        });
    }
    
    /**
     * Removes the chunk in the given world from the {@link CustomDataWorld}.
     */
    public static final void removeChunk(ServerWorld world, WorldChunk chunk) {
        getCustomDataWorld(world.getDimension()).removeChunk(chunk.getPos());
    }
    
    /**
     * CLEARS ALL THE DATA OF THE {@link ConcurrentHashMap}.
     */
    public static final void clear() {
        DIMS.clear();
    }
    
    /**
     * Gets all the data of the chunk in the given {@link ChunkPos}, in the given {@link DimensionType}.
     */
    public static final String getAllData(DimensionType dim, ChunkPos chunkPos) {
        NbtCompound tmp = new NbtCompound();
        CustomDataRegistry.forEach((id, dataType) -> {
            dataType.serialize(tmp, id, getCustomDataWorld(dim).getChunk(chunkPos).getData(id));
        });
        return tmp.toString();
    }
}
