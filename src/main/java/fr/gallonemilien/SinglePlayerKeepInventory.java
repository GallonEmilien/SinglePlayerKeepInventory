package fr.gallonemilien;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinglePlayerKeepInventory implements ModInitializer {
    public static final String MOD_ID = "singleplayerkeepinventory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String KEEP_INV_TAG = "custom_keep_inventory";

    public static final ThreadLocal<Boolean> KEEP_INV_OVERRIDE = ThreadLocal.withInitial(() -> false);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("keepinventory")
                    .requires(source -> {
                        ServerPlayer player = source.getPlayer();
                        return player == null || source.getServer().getPlayerList().isOp(player.nameAndId());
                    })
                    .then(Commands.literal("on")
                            .then(Commands.argument("player", EntityArgument.player())
                                    .executes(context -> {
                                        ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                        target.addTag(KEEP_INV_TAG);
                                        context.getSource().sendSuccess(() -> Component.literal("§aKeepInventory enabled for " + target.getScoreboardName()), false);
                                        return 1;
                                    })))
                    .then(Commands.literal("off")
                            .then(Commands.argument("player", EntityArgument.player())
                                    .executes(context -> {
                                        ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                        target.removeTag(KEEP_INV_TAG);
                                        context.getSource().sendSuccess(() -> Component.literal("§cKeepInventory disabled for " + target.getScoreboardName()), false);
                                        return 1;
                                    })))
            );
        });

        LOGGER.info("SinglePlayerKeepInventory loaded");
    }
}