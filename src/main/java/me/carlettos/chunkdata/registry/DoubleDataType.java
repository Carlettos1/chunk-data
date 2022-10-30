package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link Double}.
 */
public class DoubleDataType extends SimpleDataType<Double> {

    public DoubleDataType(Supplier<Double> supplier) {
        super(supplier);
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, Double data) {
        nbt.putDouble(id.toString(), data);
    }

    @Override
    public Double deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getDouble(id.toString());
    }
}
