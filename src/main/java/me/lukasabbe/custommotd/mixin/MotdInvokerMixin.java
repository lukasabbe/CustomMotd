package me.lukasabbe.custommotd.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(MinecraftServer.class)
public interface MotdInvokerMixin {
    @Invoker("createMetadata")
    ServerMetadata getServerMetaData();
    @Invoker("createMetadataPlayers")
    ServerMetadata.Players getPlayerData();
    @Invoker("loadFavicon")
    Optional<ServerMetadata.Favicon> getFavIconData();
    @Accessor("metadata")
    void setMetaData(ServerMetadata data);
}
