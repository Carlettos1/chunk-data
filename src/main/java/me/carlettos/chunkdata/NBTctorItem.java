package me.carlettos.chunkdata;

import me.carlettos.chunkdata.world.CustomDataManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * A custom {@link Item} that says all the custom data on the chunk in which is used.
 * Only for debug pruporses
 */
public class NBTctorItem extends Item {

    public NBTctorItem() {
        super(new Settings().group(ItemGroup.TOOLS));
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            player.sendMessage(Text.of(CustomDataManager.getAllData(world.getDimension(), player.getChunkPos())));
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}