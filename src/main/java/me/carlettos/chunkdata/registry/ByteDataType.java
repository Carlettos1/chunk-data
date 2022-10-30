package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link Byte}.
 */
public class ByteDataType extends SimpleDataType<Byte> {

    public ByteDataType(Supplier<Byte> supplier) {
        super(supplier);
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, Byte data) {
        nbt.putByte(id.toString(), data);
    }

    @Override
    public Byte deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getByte(id.toString());
    }
}
