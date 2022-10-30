package me.carlettos.chunkdata.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.carlettos.chunkdata.world.CustomDataManager;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    /**
     * Removes all the chunks in the {@link CustomDataManager} when the server closes.
     */
    @Inject(at = @At("TAIL"), method = "save")
    public void save(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (flush) {
            CustomDataManager.clear();
        }
    }
}
