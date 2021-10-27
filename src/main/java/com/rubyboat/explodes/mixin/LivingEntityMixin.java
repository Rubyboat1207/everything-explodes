package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(at = @At("HEAD"), method = "updatePostDeath", cancellable = true)
	public void updatePostDeath(CallbackInfo ci) {
		if (!((LivingEntity)(Object) this).isPlayer()) {
			if(((LivingEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.IS_TNT_GAMEMODE))
			{
				Main.spawnTNT((LivingEntity)(Object) this);
			}
			if (((LivingEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.IS_PEACE_LOVE_AND_PLANTS))
			{
				LivingEntity livingEntity = ((LivingEntity)(Object) this).getAttacker();
				if(livingEntity != null)
				{
					if(livingEntity.isPlayer())
					{
						PlayerEntity player = (PlayerEntity) livingEntity;
						Main.killPlayerIfNotCreative(player);
						if(player instanceof ServerPlayerEntity)
						{
							ServerPlayerEntity spe = (ServerPlayerEntity) player;
							spe.changeGameMode(GameMode.SPECTATOR);
						}
					}
				}
			}
		}
	}
	int CurrentTick = 0;
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	public void tick(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if(CurrentTick >= 5)
		{
			CurrentTick = 0;
			if(((LivingEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.PVZ))
			{
				Block block = entity.getEntityWorld().getBlockState(entity.getBlockPos()).getBlock();
				String blockname = block.getTranslationKey();
				switch(blockname.toLowerCase(Locale.ROOT))
				{
					case "block.minecraft.rose_bush":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 40, 1));
						break;
					case "block.minecraft.dandelion":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20, 3));
						break;
					case "block.minecraft.cornflower":
						entity.setFrozenTicks(entity.getFrozenTicks() + 20);
						break;
					case "block.minecraft.dead_bush":
						entity.setHealth(entity.getHealth() - 1);
						break;
					case  "block.minecraft.poppy":
					case "block.minecraft.lilac":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20, 3));
						break;
					case "block.minecraft.blue_orchid":
					case "block.minecraft.lily_of_the_valley":
						entity.setFrozenTicks(entity.getFrozenTicks() + 30);
						break;
					case "block.minecraft.allium":
					case "block.minecraft.pink_tulip":
					case "block.minecraft.peony":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 3));
						break;
					case "block.minecraft.azure_bluet":
					case "block.minecraft.white_tulip":
					case "block.minecraft.oxeye_daisy":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 3));
						break;
					case "block.minecraft.red_tulip":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 20, 3));
						break;
					case "block.minecraft.orange_tulip":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 3));
						break;

				}
			}
		}
		CurrentTick++;
	}
}