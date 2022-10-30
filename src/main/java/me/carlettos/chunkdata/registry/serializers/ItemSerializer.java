package me.carlettos.chunkdata.registry.serializers;

import me.carlettos.chunkdata.registry.CustomDataType;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Example of a {@link Serializer} using minecraft's {@link Item} and 
 * {@link Registry}. It can be used for a {@link CustomDataType}.
 */
public class ItemSerializer implements Serializer<Item> {

    @Override
    public void serialize(NbtCompound parent, Item item) {
        parent.putString("item", Registry.ITEM.getId(item).toString());
    }

    @Override
    public Item deserialize(NbtCompound parent) {
        return Registry.ITEM.get(new Identifier(parent.getString("item")));
    }
}
