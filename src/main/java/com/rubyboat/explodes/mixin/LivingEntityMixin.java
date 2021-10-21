package com.rubyboat.explodes.mixin;

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
	public void updatePostDeath(CallbackInfo ci)
	{
		if(!((LivingEntity)(Object)this).isPlayer())
		{
			spawnTNT((LivingEntity) (Object)this);
		}
	}
	public void spawnTNT(LivingEntity livingEntity)
	{
		if(livingEntity.deathTime >= 19)
		{
			ServerWorld serverWorld = null;
			try
			{
				serverWorld = (ServerWorld) livingEntity.getEntityWorld();
			}
			catch (Exception e)
			{
				return;
			}
			try
			{
				TntEntity tntEntity = new TntEntity(serverWorld, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), null);
				tntEntity.setFuse(40);
				serverWorld.spawnEntity(tntEntity);
			}catch(Exception e)
			{
				for(int i = serverWorld.getPlayers().size(); i < serverWorld.getPlayers().size(); i++)
				{
					if(serverWorld.getPlayers().get(i).hasPermissionLevel(2))
					{
						serverWorld.getPlayers().get(i).sendMessage(Text.of("TNT Failed to go off"), false);
					}
				}
			}

		}
	}
}
