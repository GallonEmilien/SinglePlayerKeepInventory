package fr.gallonemilien.mixin;

import fr.gallonemilien.SinglePlayerKeepInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.
        injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void onDiePre(DamageSource source, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (player.entityTags().contains(SinglePlayerKeepInventory.KEEP_INV_TAG)) {
            SinglePlayerKeepInventory.KEEP_INV_OVERRIDE.set(true);
        }
    }

    @Inject(method = "die", at = @At("RETURN"))
    private void onDiePost(DamageSource source, CallbackInfo ci) {
        SinglePlayerKeepInventory.KEEP_INV_OVERRIDE.set(false);
    }

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    private void onRestorePre(ServerPlayer oldPlayer, boolean restoreAll, CallbackInfo ci) {
        if (oldPlayer.entityTags().contains(SinglePlayerKeepInventory.KEEP_INV_TAG)) {
            SinglePlayerKeepInventory.KEEP_INV_OVERRIDE.set(true);
        }
    }

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void onRestorePost(ServerPlayer oldPlayer, boolean restoreAll, CallbackInfo ci) {
        SinglePlayerKeepInventory.KEEP_INV_OVERRIDE.set(false);
    }

    @Inject(method = "doTick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        SinglePlayerKeepInventory.KEEP_INV_OVERRIDE.set(false);
    }
}