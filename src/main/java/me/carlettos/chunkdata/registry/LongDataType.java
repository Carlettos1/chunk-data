package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link Long}.
 */
public class LongDataType extends SimpleDataType<Long> {

    public LongDataType(Supplier<Long> supplier) {
        super(supplier);
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, Long data) {
        nbt.putLong(id.toString(), data);
    }

    @Override
    public Long deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getLong(id.toString());
    }
}
