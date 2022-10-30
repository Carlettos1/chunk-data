package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

/**
 * A {@link SimpleDataType} is a {@link DataType} which uses the id 
 * of the registered DataType as the key in the nbt.
 * 
 * @see CustomDataType
 * @see BooleanDataType
 * @see ByteDataType
 * @see DoubleDataType
 * @see FloatDataType
 * @see IntDataType
 * @see LongDataType
 * @see ShortDataType
 * @see StringDataType
 */
public abstract class SimpleDataType<T> implements DataType<T> {
    private final Supplier<T> supplier;
    
    protected SimpleDataType(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    @Override
    public T generate(ChunkPos chunkPos) {
        return supplier.get();
    }
    
    @Override
    public boolean isIn(NbtCompound nbt, Identifier id) {
        return nbt.contains(id.toString());
    }
}
