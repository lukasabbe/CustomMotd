package me.lukasabbe.custommotd.util;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import me.lukasabbe.custommotd.Custommotd;
import me.lukasabbe.custommotd.mixin.MotdInvokerMixin;
import net.minecraft.server.ServerMetadata;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.lukasabbe.custommotd.Custommotd.*;

public class MetaData {
    public static ServerMetadata createServerData(@Nullable String motd, boolean b){
        Text _motd = Text.of(config.Motd);
        Runnable r = () -> {
            if(config.linkToPhoto != null)
                MetaData.setByteArrayFromLink(config.linkToPhoto);
        };
        THREAD_POOL_EXECUTOR.submit(r);
        if(config.Motd == null)
            _motd = Text.of(motd);
        else{
            _motd = TextParser.formatText(_motd.getString());
        }
        return new ServerMetadata(Text.of(_motd),getPlayerList(), Optional.of(ServerMetadata.Version.create()),getFavIcon(),b);
    }

    private static Optional<ServerMetadata.Players> getPlayerList() {
        ServerMetadata.Players players = ((MotdInvokerMixin) server).getPlayerData();
        int onlinePlayers = config.currentPlayerCount;
        int maxPlayer = config.maxPlayerCount;
        List<String> playerStrings = config.playerList;
        if (config.currentPlayerCount == -1)
            onlinePlayers = players.online();
        if (config.maxPlayerCount == -1)
            maxPlayer = players.max();
        if (config.playerList == null) {
            playerStrings = new ArrayList<>();
            for (GameProfile profile : players.sample()) {
                playerStrings.add(profile.getName());
            }
        }
        List<GameProfile> parsedStrings = new ArrayList<>();
        try{
            for (String p : playerStrings) {
                parsedStrings.add(new GameProfile(UUID.randomUUID(), TextParser.formatText(p).getString()));
            }
        }catch (ClassCastException exception){
            LOGGER.error("Make sure to use \"\" around any alone number in the player-list. Like \"1\"");
            parsedStrings.add(new GameProfile(UUID.randomUUID(), "Check erros"));
        }
        return Optional.of(new ServerMetadata.Players(maxPlayer, onlinePlayers, parsedStrings));
    }
    private static Optional<ServerMetadata.Favicon> getFavIcon(){
        Optional<ServerMetadata.Favicon> favicon = ((MotdInvokerMixin)server).getFavIconData();
        byte[] iconBytes;
        if(config.linkToPhoto != null && configFavicon != null)
            iconBytes = configFavicon.iconBytes();
        else
            return favicon;
        return Optional.of(new ServerMetadata.Favicon(iconBytes));
    }
    public static void setByteArrayFromLink(String linkToPhoto){
        try{
            URI uri = new URI(linkToPhoto);
            URL url = uri.toURL();
            BufferedImage bufferedImage = ImageIO.read(url);
            Preconditions.checkState(bufferedImage.getWidth() == 64, "Must be 64 pixels wide");
            Preconditions.checkState(bufferedImage.getHeight() == 64, "Must be 64 pixels high");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG",byteArrayOutputStream);
            configFavicon = new ServerMetadata.Favicon(byteArrayOutputStream.toByteArray());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
