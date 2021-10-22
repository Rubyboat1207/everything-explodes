package com.rubyboat.explodes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("modid");
	public static final GameRules.Key<GameRules.IntRule> FUSE_TICKS = GameRuleRegistry.register(
			"fuseTicks",
			GameRules.Category.MOBS,
			GameRuleFactory.createIntRule(40, 0)
	);
	public static final GameRules.Key<GameRules.IntRule> TNT_POWER = GameRuleRegistry.register(
			"tntPower",
			GameRules.Category.MOBS,
			GameRuleFactory.createIntRule(4, 0)
	);
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}
