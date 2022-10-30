package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link Short}.
 */
public class ShortDataType extends SimpleDataType<Short> {

    public ShortDataType(Supplier<Short> supplier) {
        super(supplier);
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, Short data) {
        nbt.putShort(id.toString(), data);
    }

    @Override
    public Short deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getShort(id.toString());
    }
}
