package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tick(CallbackInfo ci) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        if(((LivingEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.NO_FALL)) {
            playerEntity.setNoGravity(true);
        }
        if(((LivingEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.PVZ))
        {
            LivingEntity livingEntity = (LivingEntity) playerEntity;
            EntityAttributeInstance entityAttributeInstance = livingEntity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            entityAttributeInstance.setBaseValue(-100);
        }
    }
    @Inject(at = @At("HEAD"), method = "checkFallFlying", cancellable = true)
    public void checkflight(CallbackInfoReturnable ci)
    {
        /*
        if(((LivingEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.ONLY_FLIGHTS))
        {
            ((PlayerEntity)(Object) this).startFallFlying();
            ci.setReturnValue(true);
            ci.cancel();
        }
         */
    }
}
