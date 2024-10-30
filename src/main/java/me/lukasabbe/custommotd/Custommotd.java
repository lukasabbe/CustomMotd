package me.lukasabbe.custommotd;

import me.lukasabbe.custommotd.commands.ReloadCommand;
import me.lukasabbe.custommotd.config.Config;
import me.lukasabbe.custommotd.util.MetaData;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Custommotd implements DedicatedServerModInitializer {
    public final static String MOD_ID = "custommotd";
    public static Config config;
    public static MinecraftServer server;
    public static ServerMetadata.Favicon configFavicon;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    @Override
    public void onInitializeServer() {
        config = new Config();
        CommandRegistrationCallback.EVENT.register(ReloadCommand::registerCommand);
        Runnable r = () -> {
            if(config.linkToPhoto != null)
              MetaData.setByteArrayFromLink(config.linkToPhoto);
        };
        THREAD_POOL_EXECUTOR.submit(r);
    }

}
