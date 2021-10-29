package com.rubyboat.explodes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class Main implements ModInitializer {
	/*
	LIST OF GAMEMODES:
	Dehidration - No Touch Water
	OverHydrated - Must Be inside water
	vegan - No Eat Meat & No Killing Animals
	Peace Love & Plants - No Killing or u become spectator
	PVZ - No Dealing Damage, but you can place plants
	NoFall - You Cant Fall
	Celina - Hell & always night
	Unfair - WHY??!?!?!?
	 */
	public static final CustomGameRuleCategory GAMEMODES = new CustomGameRuleCategory(new Identifier("gamemodes", "gamemodes"), Text.of("Gamemodes"));

	public static final GameRules.Key<GameRules.IntRule> FUSE_TICKS = GameRuleRegistry.register(
			"fuseTicks",
			GAMEMODES,
			GameRuleFactory.createIntRule(40, 0)
	);
	public static final GameRules.Key<GameRules.IntRule> TNT_POWER = GameRuleRegistry.register(
			"tntPower",
			GameRules.Category.MOBS,
			GameRuleFactory.createIntRule(4, 0)
	);
	public static final GameRules.Key<GameRules.BooleanRule> IS_TNT_GAMEMODE = GameRuleRegistry.register(
			"isTNTGamemode",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> Unfair = GameRuleRegistry.register(
			"isUnfair",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> IS_VEGAN_GAMEMODE = GameRuleRegistry.register(
			"isVeganGamemode",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> IS_PEACE_LOVE_AND_PLANTS = GameRuleRegistry.register(
			"isPeaceLoveAndPlantsGamemode",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> NO_FALL = GameRuleRegistry.register(
			"isNoFlightGamemode",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> PVZ = GameRuleRegistry.register(
			"isPVZGamemode",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> DEHYDRATED = GameRuleRegistry.register(
			"isDehydratedGamemode",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> OVERHYDRATED = GameRuleRegistry.register(
			"isOverhydratedGamemode",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.IntRule> TAX_TIME = GameRuleRegistry.register(
			"TaxInterval",
			GAMEMODES,
			GameRuleFactory.createIntRule(8400, 0, 12000)
	);
	public static final ArrayList<Item> MEAT_LIST = new ArrayList<>(Arrays.asList(
			Items.COOKED_BEEF,
			Items.BEEF,
			Items.MUTTON,
			Items.COOKED_MUTTON,
			Items.PORKCHOP,
			Items.COD,
			Items.COOKED_COD,
			Items.SALMON,
			Items.COOKED_SALMON,
			Items.PUFFERFISH,
			Items.ROTTEN_FLESH,
			Items.TROPICAL_FISH,
			Items.CHICKEN,
			Items.COOKED_CHICKEN,
			Items.RABBIT,
			Items.COOKED_RABBIT,
			Items.MILK_BUCKET,
			Items.POTION
	));
	public static void spawnTNT(LivingEntity livingEntity) {
		if (livingEntity.deathTime >= 19) {

			if (livingEntity.getEntityWorld() instanceof ServerWorld ^ livingEntity instanceof EnderDragonEntity) {
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
	public static boolean isInWater(BlockState blockState)
	{
		if(blockState.getFluidState().isIn(FluidTags.WATER))
		{
			return true;
		}
		if(blockState.getBlock() == Blocks.WATER ^ blockState.getBlock() == Blocks.WATER_CAULDRON)
		{
			return true;
		}
		return false;
	}
	public static void killPlayerIfNotCreative(PlayerEntity player)
	{
		if(!player.isCreative())
		{
			player.kill();
		}
	}
	public static float GenerateRandomNumber(int min, int max)
	{
		return (float) Math.floor(Math.random()*(max-min+1)+min);
	}

	@Override
	public void onInitialize() {

	}
}
