package me.carlettos.chunkdata.registry.serializers;

import me.carlettos.chunkdata.registry.CustomDataType;
import net.minecraft.nbt.NbtCompound;

/**
 * A way to reduce the code by reducing the serializing process to these two methods.
 * 
 * @see CustomDataType
 */
public interface Serializer<T> {
    
    /**
     * Writes the given object to the given {@link NbtCompound}.
     * 
     * @param parent parent {@link NbtCompound} with no other info (is not the chunk {@link NbtCompound})
     * @param obj {@link T} object to serialize
     */
    void serialize(NbtCompound parent, T obj);
    
    /**
     * Reads the {@link NbtCompound} and returns an object.
     * 
     * @param parent parent {@link NbtCompound} with only the info to deserialize (is not the chunk {@link NbtCompound})
     */
    T deserialize(NbtCompound parent);
}
