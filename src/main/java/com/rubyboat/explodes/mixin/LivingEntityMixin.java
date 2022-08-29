package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow public abstract void enterCombat();

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
	int flowertick = 0;
	int skeletonTick = 0;
	Boolean hasSpawnedWither = false;
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	public void tick(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		World world = entity.getEntityWorld();
		if(entity instanceof EnderDragonEntity)
		{
			if(entity.getEntityWorld().getGameRules().getBoolean(Main.Unfair))
			{
				if(entity.getHealth() <= (entity.getMaxHealth() / 3) * 2)
				{
					if(!hasSpawnedWither)
					{
						hasSpawnedWither = true;
						WitherEntity wither = new WitherEntity(EntityType.WITHER, entity.getEntityWorld());
						wither.setPos(entity.prevX, entity.prevY + 20, entity.prevZ);
						world.spawnEntity(wither);
					}
				}
				skeletonTick++;
				if(skeletonTick / 20 >= 2)
				{
					skeletonTick = 0;
					SkeletonEntity skele = new SkeletonEntity(EntityType.SKELETON, entity.getEntityWorld());
					skele.setCustomName(Text.of("SANS UNDERTALE"));
					skele.setCustomNameVisible(true);
					skele.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(1);
					skele.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(80);
					skele.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
					skele.setHealth(80);
					skele.setPos(entity.prevX, entity.prevY - 4, entity.prevZ);
					world.spawnEntity(skele);
				}
			}
		}



		if(flowertick >= 5)
		{
			flowertick = 0;
			if(((LivingEntity)(Object) this).getEntityWorld().getGameRules().getBoolean(Main.PVZ))
			{
				Block block = world.getBlockState(entity.getBlockPos()).getBlock();
				String blockname = block.getTranslationKey();
				switch(blockname.toLowerCase(Locale.ROOT))
				{
					case "block.minecraft.rose_bush":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 40, 2));
						break;
					case "block.minecraft.dandelion":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 80, 3));
						break;
					case "block.minecraft.cornflower":
						entity.setFrozenTicks(entity.getFrozenTicks() + 20);
						entity.setHealth(entity.getHealth() - 1);
						break;
					case "block.minecraft.dead_bush":
						entity.setHealth(entity.getHealth() - 2);
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
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 3));
						break;
					case "block.minecraft.azure_bluet":
					case "block.minecraft.white_tulip":
					case "block.minecraft.oxeye_daisy":
						entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 3));
						entity.setFireTicks(entity.getFireTicks() + 20);

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
		flowertick++;
	}
}