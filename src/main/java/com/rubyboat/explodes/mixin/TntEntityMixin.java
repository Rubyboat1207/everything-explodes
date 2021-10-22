package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin {
    public float power = 4.0f;
    @Inject(at = @At("HEAD"), method = "explode", cancellable = true)
    private void explode(CallbackInfo ci) {
        TntEntity tntEntity = (TntEntity) (Object) this;
        tntEntity.world.createExplosion(tntEntity, tntEntity.getX(), tntEntity.getBodyY(0.0625D), tntEntity.getZ(), tntEntity.getEntityWorld().getGameRules().getInt(Main.TNT_POWER), Explosion.DestructionType.BREAK);
    }
}
