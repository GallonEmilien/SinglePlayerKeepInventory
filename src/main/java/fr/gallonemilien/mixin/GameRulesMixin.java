package fr.gallonemilien.mixin;

import fr.gallonemilien.SinglePlayerKeepInventory;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRules.class)
public abstract class GameRulesMixin {

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private <T> void overrideKeepInventory(GameRule<@NotNull T> gameRule, CallbackInfoReturnable<T> cir) {
        if (gameRule == GameRules.KEEP_INVENTORY && SinglePlayerKeepInventory.KEEP_INV_OVERRIDE.get()) {
            cir.setReturnValue((T) Boolean.TRUE);
        }
    }
}