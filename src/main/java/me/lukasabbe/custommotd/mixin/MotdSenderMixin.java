package me.lukasabbe.custommotd.mixin;

import com.mojang.datafixers.DataFixer;
import me.lukasabbe.custommotd.Custommotd;
import me.lukasabbe.custommotd.util.MetaData;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.ApiServices;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public abstract class MotdSenderMixin {
    @Shadow @Nullable private String motd;

    @Shadow public abstract boolean shouldEnforceSecureProfile();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void slide(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        Custommotd.server = (MinecraftServer) (Object) this;
    }

    @Inject(method = "createMetadata", at=@At("HEAD"), cancellable = true)
    public void createMetaData(CallbackInfoReturnable<ServerMetadata> cir){
        ServerMetadata _metadata = MetaData.createServerData(motd, shouldEnforceSecureProfile());
        cir.setReturnValue(_metadata);
    }

}
