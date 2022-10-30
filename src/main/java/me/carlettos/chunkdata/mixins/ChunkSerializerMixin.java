package me.carlettos.chunkdata.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.carlettos.chunkdata.registry.CustomDataRegistry;
import me.carlettos.chunkdata.registry.DataType;
import me.carlettos.chunkdata.world.CustomDataChunk;
import me.carlettos.chunkdata.world.CustomDataManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestStorage;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {
    
    /**
     * Writes the custom data to the chunk's NBT.
     * 
     * If the {@link CustomDataManager} doesn't have the chunk, 
     * then it has to be generated using {@link CustomDataManager#generateData(DimensionType, ChunkPos)}.
     * This is primarly done because a new generated {@link WorldChunk} is serialized first, and then might be deserialized.
     * 
     */
    @Inject(at = @At("RETURN"), method = "serialize", cancellable = true)
    private static void serialize(ServerWorld world, Chunk chunk, CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound chunkData = cir.getReturnValue();
        DimensionType dim = world.getDimension();
        ChunkPos chunkPos = chunk.getPos();
        
        if (!CustomDataManager.hasChunk(dim, chunkPos)) {
            CustomDataManager.generateData(dim, chunkPos);
        }
        
        CustomDataRegistry.forEach((id, dataType) -> {
            dataType.serialize(chunkData, id, CustomDataManager.getData(world, chunkPos, id));
        });
        cir.setReturnValue(chunkData);
    }

    /**
     * Reads the custom data from the chunk's NBT.
     * 
     * If its trying to read a chunk that is currently managed by {@link CustomDataManager}, then it does nothing.
     * <p>
     * Creates a new empty {@link CustomDataChunk} to fill with all the readed data.
     * If a {@link DataType} is not in the {@link CustomDataRegistry}, then generates the data using {@link DataType#generate(ChunkPos)} method. 
     * This is because a mod may register a {@link DataType} later and needs to act retroactively for all the chunks. Or because a new mod
     * is added, therefore needs the custom data to be created.
     */
    @SuppressWarnings("deprecation")
    @Inject(at = @At("RETURN"), method = "deserialize")
    private static void deserialize(ServerWorld world, PointOfInterestStorage poiStorage, ChunkPos chunkPos, NbtCompound chunkData, CallbackInfoReturnable<ProtoChunk> cir) {
        DimensionType dim = world.getDimension();
        if (CustomDataManager.hasChunk(dim, chunkPos)) {
        } else { 
            CustomDataManager.getCustomDataWorld(dim).setChunk(chunkPos, new CustomDataChunk(chunkPos));
            CustomDataRegistry.forEach((id, dataType) -> {
                Object data;
                if (dataType.isIn(chunkData, id)) {
                    data = dataType.deserialize(chunkData, id);
                } else {
                    data = dataType.generate(chunkPos);
                }
                CustomDataManager.setDataNoSave(dim, chunkPos, id, data);
            });
        }
    }
}
