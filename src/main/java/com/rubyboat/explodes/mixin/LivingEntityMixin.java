package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(at = @At("HEAD"), method = "updatePostDeath", cancellable = true)
	public void updatePostDeath(CallbackInfo ci) {
		if (!((LivingEntity)(Object) this).isPlayer()) {
			Main.spawnTNT((LivingEntity)(Object) this);
		}
	}





}