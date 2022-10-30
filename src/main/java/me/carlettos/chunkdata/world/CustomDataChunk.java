package me.carlettos.chunkdata.world;

import java.util.LinkedHashMap;

import me.carlettos.chunkdata.registry.serializers.Serializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

/**
 * Representation of a minecraft {@link WorldChunk}, but with all the custom data in it.
 * 
 * A {@link Serializer} has to be able to get the object of {@link CustomDataChunk#customData}
 * ({@link LinkedHashMap}) and serialize it (and deserialize it). 
 * Better use {@link CustomDataManager} for accesing its data.
 * 
 * @see Serializer
 */
public class CustomDataChunk {
    protected final LinkedHashMap<Identifier, Object> customData = new LinkedHashMap<>();
    protected final ChunkPos pos;
    
    public CustomDataChunk(final ChunkPos pos) {
        this.pos = pos;
    }
    
    public <T> void setData(final Identifier id, final T data) {
        this.customData.put(id, data);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getData(final Identifier id) {
        return (T) customData.get(id);
    }
    
    public boolean hasData(final Identifier id) {
        return customData.containsKey(id);
    }
    
    public ChunkPos getPos() {
        return pos;
    }
}
