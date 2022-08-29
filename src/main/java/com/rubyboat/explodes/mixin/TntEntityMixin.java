package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin {

    @Inject(at = @At("HEAD"), method = "explode", cancellable = true)
    private void explode(CallbackInfo ci) {
        TntEntity tntEntity = (TntEntity) (Object) this;
        tntEntity.world.createExplosion(tntEntity, tntEntity.getX(), tntEntity.getBodyY(0.0625D), tntEntity.getZ(), tntEntity.getDataTracker().get(Main.POWER), Explosion.DestructionType.BREAK);
    }

    @Inject(at = @At("HEAD"), method = "initDataTracker", cancellable = true)
    private void initDataTracker(CallbackInfo ci) {
        TntEntity tntEntity = (TntEntity) (Object) this;
        tntEntity.getDataTracker().startTracking(Main.POWER, 4);
    }
}
