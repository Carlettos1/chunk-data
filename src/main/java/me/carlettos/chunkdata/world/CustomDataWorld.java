package me.carlettos.chunkdata.world;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

/**
 * A {@link World} representation with {@link CustomDataChunk} in it.
 * Better use {@link CustomDataManager} for accessing its data.
 */
public class CustomDataWorld {
    protected final ConcurrentHashMap<ChunkPos, CustomDataChunk> chunks = new ConcurrentHashMap<>();

    public void setChunk(final ChunkPos pos, final CustomDataChunk chunkData) {
        this.chunks.put(pos, chunkData);
    }
    
    public CustomDataChunk getChunk(final ChunkPos pos) {
        return this.chunks.get(pos);
    }
    
    public boolean hasChunk(final ChunkPos pos) {
        return this.chunks.containsKey(pos);
    }
    
    public void removeChunk(final ChunkPos pos) {
        this.chunks.remove(pos);
    }
    
    public <T> void setData(final Identifier id, final T data, final ChunkPos pos) {
        this.getChunk(pos).setData(id, data);
    }
    
    public <T> T getData(final Identifier id, final ChunkPos pos) {
        return this.getChunk(pos).getData(id);
    }
    
    public boolean hasData(final Identifier id, final ChunkPos pos) {
        return this.getChunk(pos).hasData(id);
    }
    
    public int size() {
        return chunks.size();
    }
}
