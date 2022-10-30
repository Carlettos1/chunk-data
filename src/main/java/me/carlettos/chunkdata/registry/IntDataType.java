package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link Integer}.
 */
public class IntDataType extends SimpleDataType<Integer> {

    public IntDataType(Supplier<Integer> supplier) {
        super(supplier);
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, Integer data) {
        nbt.putInt(id.toString(), data);
    }

    @Override
    public Integer deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getInt(id.toString());
    }
}
