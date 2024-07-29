package me.lukasabbe.custommotd;

import com.mojang.authlib.GameProfile;
import me.lukasabbe.custommotd.commands.ReloadCommand;
import me.lukasabbe.custommotd.config.Config;
import me.lukasabbe.custommotd.mixin.MotdInvokerMixin;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Custommotd implements DedicatedServerModInitializer {
    public final static String MOD_ID = "custommotd";
    public static Config config;
    public static MinecraftServer server;
    @Override
    public void onInitializeServer() {
        config = new Config();
        CommandRegistrationCallback.EVENT.register(ReloadCommand::registerCommand);
    }
    public static ServerMetadata createServerData(@Nullable String motd, boolean b){
        String _motd = config.Motd;
        if(config.Motd == null)
            _motd = motd;
        return new ServerMetadata(Text.of(_motd),getPlayerList(),Optional.of(ServerMetadata.Version.create()),getFavIcon(),b);
    }
    private static Optional<ServerMetadata.Players> getPlayerList(){
        ServerMetadata.Players players = ((MotdInvokerMixin)server).getPlayerData();
        int onlinePlayers = config.currentPlayerCount;
        int maxPlayer = config.maxPlayerCount;
        List<String> playerStrings = config.playerList;
        if(config.currentPlayerCount == -1)
            onlinePlayers = players.online();
        if(config.maxPlayerCount == -1)
            maxPlayer = players.max();
        if(config.playerList == null){
            playerStrings = new ArrayList<>();
            for(GameProfile profile: players.sample()){
                playerStrings.add(profile.getName());
            }
        }
        List<GameProfile> parsedStrings = new ArrayList<>();
        for(String p : playerStrings){
            parsedStrings.add(new GameProfile(UUID.randomUUID(),p));
        }
        return Optional.of(new ServerMetadata.Players(maxPlayer,onlinePlayers,parsedStrings));
    }
    private static Optional<ServerMetadata.Favicon> getFavIcon(){
        Optional<ServerMetadata.Favicon> favicon = ((MotdInvokerMixin)server).getFavIconData();
        byte[] iconBytes;
        if(config.linkToPhoto != null)
            iconBytes = getByteArrayFromLink(config.linkToPhoto);
        else
            return favicon;
        return Optional.of(new ServerMetadata.Favicon(iconBytes));
    }
    private static byte[] getByteArrayFromLink(String linkToPhoto){
        return null;
    }
}
