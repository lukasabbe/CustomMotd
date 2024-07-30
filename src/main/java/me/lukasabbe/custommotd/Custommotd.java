package me.lukasabbe.custommotd;

import me.lukasabbe.custommotd.commands.ReloadCommand;
import me.lukasabbe.custommotd.config.Config;
import me.lukasabbe.custommotd.util.MetaData;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;

public class Custommotd implements DedicatedServerModInitializer {
    public final static String MOD_ID = "custommotd";
    public static Config config;
    public static MinecraftServer server;
    public static ServerMetadata.Favicon configFavicon;
    @Override
    public void onInitializeServer() {
        config = new Config();
        CommandRegistrationCallback.EVENT.register(ReloadCommand::registerCommand);
        Runnable r = () -> {
            if(config.linkToPhoto != null)
              MetaData.setByteArrayFromLink(config.linkToPhoto);
        };
        Thread thread = new Thread(r);
        thread.start();
    }
}
