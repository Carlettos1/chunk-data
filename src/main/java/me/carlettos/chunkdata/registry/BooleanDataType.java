package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link Boolean}.
 */
public class BooleanDataType extends SimpleDataType<Boolean> {

    public BooleanDataType(Supplier<Boolean> supplier) {
        super(supplier);
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, Boolean data) {
        nbt.putBoolean(id.toString(), data);
    }

    @Override
    public Boolean deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getBoolean(id.toString());
    }
}
