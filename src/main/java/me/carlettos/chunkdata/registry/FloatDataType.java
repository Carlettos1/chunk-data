package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link Float}.
 */
public class FloatDataType extends SimpleDataType<Float> {

    public FloatDataType(Supplier<Float> supplier) {
        super(supplier);
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, Float data) {
        nbt.putFloat(id.toString(), data);
    }

    @Override
    public Float deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getFloat(id.toString());
    }
}
