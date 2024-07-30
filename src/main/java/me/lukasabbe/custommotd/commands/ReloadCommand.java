package me.lukasabbe.custommotd.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.lukasabbe.custommotd.Custommotd;
import me.lukasabbe.custommotd.mixin.MotdInvokerMixin;
import me.lukasabbe.custommotd.util.MetaData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ReloadCommand {

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("reloadmotd").requires(p -> p.hasPermissionLevel(2)).executes(ReloadCommand::runCommand));
    }

    private static int runCommand(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        Custommotd.config.reloadConfig();
        Runnable r = () -> {
            if(Custommotd.config.linkToPhoto != null)
                MetaData.setByteArrayFromLink(Custommotd.config.linkToPhoto);
            ServerMetadata metadata = ((MotdInvokerMixin)Custommotd.server).getServerMetaData();
            ((MotdInvokerMixin)Custommotd.server).setMetaData(metadata);
        };
        Thread thread = new Thread(r);
        thread.start();
        serverCommandSourceCommandContext.getSource().sendFeedback(()->Text.literal("Reloded config"),true);
        return 0;
    }
}
