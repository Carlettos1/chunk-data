package me.carlettos.chunkdata.registry.serializers;

import me.carlettos.chunkdata.registry.CustomDataType;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Example of a {@link Serializer} using minecraft's {@link Block} and 
 * {@link Registry}. It can be used for a {@link CustomDataType}.
 */
public class BlockSerializer implements Serializer<Block> {

    @Override
    public void serialize(NbtCompound parent, Block block) {
        parent.putString("block", Registry.BLOCK.getId(block).toString());
    }

    @Override
    public Block deserialize(NbtCompound parent) {
        return Registry.BLOCK.get(new Identifier(parent.getString("block")));
    }
}
