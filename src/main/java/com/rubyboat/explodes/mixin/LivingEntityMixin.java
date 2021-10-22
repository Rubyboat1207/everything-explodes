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
			spawnTNT((LivingEntity)(Object) this);
		}
	}
	public void spawnTNT(LivingEntity livingEntity) {
		if (livingEntity.deathTime >= 19) {

			if (livingEntity.getEntityWorld() instanceof ServerWorld) {
				ServerWorld serverWorld = (ServerWorld) livingEntity.getEntityWorld();
				TntEntity tntEntity = new TntEntity(serverWorld, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), null);
				tntEntity.setFuse(serverWorld.getGameRules().getInt(Main.FUSE_TICKS));
				serverWorld.spawnEntity(tntEntity);
				//debug
				for (int i = serverWorld.getPlayers().size(); i < serverWorld.getPlayers().size(); i++) {
					if (serverWorld.getPlayers().get(i).hasPermissionLevel(2)) {
						serverWorld.getPlayers().get(i).sendMessage(Text.of("TNT Failed to go off"), false);
					}
				}
			}

		}

	}
}