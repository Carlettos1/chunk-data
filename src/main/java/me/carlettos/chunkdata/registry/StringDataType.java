package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * {@link DataType} wrapper for a {@link String}.
 */
public class StringDataType extends SimpleDataType<String> {
    
    public StringDataType(Supplier<String> supplier) {
        super(supplier);
    }
    
    @Override
    public void serialize(NbtCompound nbt, Identifier id, String data) {
        nbt.putString(id.toString(), data);
    }

    @Override
    public String deserialize(NbtCompound nbt, Identifier id) {
        return nbt.getString(id.toString());
    }
}
