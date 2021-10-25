package com.rubyboat.explodes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
	/*
	LIST OF GAMEMODES:
	Dehidration - No Touch Water
	OverHydrated - Must Be inside water
	vegan - No Eat Meat & No Killing Animals
	Peace Love & Plants - No Killing or u become spectator
	PVZ - No Dealing Damage, but you can place plants
	NoFall - You Cant Fall
	OnlyFlight - Stuck In Elytra Flight Mode
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
	public static final GameRules.Key<GameRules.BooleanRule> NO_FALL = GameRuleRegistry.register(
			"isNoFlight",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
	public static final GameRules.Key<GameRules.BooleanRule> PVZ = GameRuleRegistry.register(
			"isPVZ",
			GAMEMODES,
			GameRuleFactory.createBooleanRule(false)
	);
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

	@Override
	public void onInitialize() {

	}
}
