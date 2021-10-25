package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntity.class)
public class DragonEntityMixin {
    @Inject(at = @At("HEAD"), method = "updatePostDeath", cancellable = true)
    public void updatePostDeath(CallbackInfo ci)
    {
        if(((EnderDragonEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.IS_TNT_GAMEMODE))
        {
            EnderDragonEntity enderDragonEntity = (EnderDragonEntity)(Object) this;
            World world = enderDragonEntity.getEntityWorld();
            Main.spawnTNT(enderDragonEntity);
            world.createExplosion(enderDragonEntity, enderDragonEntity.prevX, enderDragonEntity.prevY, enderDragonEntity.prevZ, 10, Explosion.DestructionType.BREAK);
        }
    }
}
