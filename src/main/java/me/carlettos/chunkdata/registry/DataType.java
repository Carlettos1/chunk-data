package me.carlettos.chunkdata.registry;

import me.carlettos.chunkdata.world.CustomDataManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;

/**
 * A Custom Data Type is a structure that serialize, deserialize and generate
 * the Object {@link T}.
 * 
 * Any Simple Data Type might be in better use extending {@link SimpleDataType}.
 * <p>
 * If you wish to register the instance, use {@link CustomDataRegistry#register(Identifier, DataType)}.
 * 
 * @see CustomDataType
 * @see SimpleDataType
 * @see BooleanDataType
 * @see ByteDataType
 * @see DoubleDataType
 * @see FloatDataType
 * @see IntDataType
 * @see LongDataType
 * @see ShortDataType
 * @see StringDataType
 */
public interface DataType<T> {
    
    /**
     * Puts the data into the NBT. It might or not use the id of the registered DataType.
     * 
     * @param nbt NBT of the chunk
     * @param id ID of the registered DataType
     * @param data Data to serialize
     */
    void serialize(NbtCompound nbt, Identifier id, T data);
    
    /**
     * Gets the data from the NBT. It might or not use the id of the registered DataType.
     * 
     * @param nbt NBT of the chunk
     * @param id ID of the registered DataType
     */
    T deserialize(NbtCompound nbt, Identifier id);
    
    /**
     * Returns whenever the given NBT contains this DataType. It might or not use the id of the registered DataType.
     * 
     * @param nbt NBT of the chunk
     * @param id ID of the registered DataType
     */
    boolean isIn(NbtCompound nbt, Identifier id);
    
    /**
     * Generates the data, used whenever {@link #isIn(NbtCompound, Identifier)} gives false
     * and in {@link CustomDataManager#generateData(DimensionType, ChunkPos)}, which is used when the
     * {@link CustomDataManager} needs to create a chunk from scratch.
     * 
     * @param chunkPos chunk position of the chunk.
     */
    T generate(ChunkPos chunkPos);
}
