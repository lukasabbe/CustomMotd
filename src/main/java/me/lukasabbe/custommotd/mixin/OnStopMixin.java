package me.lukasabbe.custommotd.mixin;

import me.lukasabbe.custommotd.Custommotd;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class OnStopMixin {
    @Inject(method = "stop", at=@At("HEAD"))
    public void onStop(boolean waitForShutdown, CallbackInfo ci){
        Custommotd.THREAD_POOL_EXECUTOR.shutdown();
    }
}
