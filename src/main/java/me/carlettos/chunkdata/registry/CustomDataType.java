package me.carlettos.chunkdata.registry;

import java.util.function.Supplier;

import me.carlettos.chunkdata.registry.serializers.Serializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * A basic implementation of {@link SimpleDataType} using a {@link Serializer} 
 * object to perform both serialization process.
 */
public class CustomDataType<T> extends SimpleDataType<T> {
    private final Serializer<T> serializer;
    
    public CustomDataType(Supplier<T> generator, Serializer<T> serializer) {
        super(generator);
        this.serializer = serializer;
    }

    @Override
    public void serialize(NbtCompound nbt, Identifier id, T data) {
        try {
            NbtCompound parent = new NbtCompound();
            this.serializer.serialize(parent, data);
            nbt.put(id.toString(), parent);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T deserialize(NbtCompound nbt, Identifier id) {
        try {
            return this.serializer.deserialize(nbt.getCompound(id.toString()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
