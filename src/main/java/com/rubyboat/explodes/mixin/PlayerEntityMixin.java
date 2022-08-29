package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Shadow public abstract boolean isPlayer();

    public int OUT_OF_WATER_TIME = 0;

    public int getWaterTime() {
        System.out.println("Got" + OUT_OF_WATER_TIME);
        return OUT_OF_WATER_TIME;
    }

    public void setWaterTime(int time) {
        OUT_OF_WATER_TIME = time;
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci)
    {
        nbt.putInt("OutOfWaterTime",OUT_OF_WATER_TIME);
    }
    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    public void readCustomDataToNbt(NbtCompound nbt, CallbackInfo ci)
    {
        OUT_OF_WATER_TIME = nbt.getInt("OutOfWaterTime");
    }
    boolean wasAlive;
    int taxtime;
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tick(CallbackInfo ci) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        GameRules gr = ((LivingEntity)(Object) this).getEntityWorld().getGameRules();
        BlockState block = playerEntity.getEntityWorld().getBlockState(playerEntity.getBlockPos());
        if(gr.getBoolean(Main.Unfair))
        {
            taxtime++;
            if(taxtime >= gr.getInt(Main.TAX_TIME))
            {
                taxtime = 0;
                playerEntity.getInventory().setStack((int) Main.GenerateRandomNumber(0, 41), new ItemStack(Items.AIR));
            }
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 40, 0));
        }

        if(!wasAlive && playerEntity.isAlive())
        {
            if(playerEntity instanceof ServerPlayerEntity)
            {
                if(gr.getBoolean(Main.OVERHYDRATED))
                {
                    playerEntity.getEntityWorld().setBlockState(playerEntity.getBlockPos(), Blocks.WATER.getDefaultState());
                }
            }
        }
        if(playerEntity.isAlive())
        {
            wasAlive = true;
        }else
        {
            wasAlive = false;
        }

        if(gr.getBoolean(Main.NO_FALL)) {
            playerEntity.setNoGravity(true);
        }
        if(gr.getBoolean(Main.PVZ))
        {
            LivingEntity livingEntity = (LivingEntity) playerEntity;
            EntityAttributeInstance entityAttributeInstance = livingEntity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            entityAttributeInstance.setBaseValue(-100);
        }
        if(gr.getBoolean(Main.OVERHYDRATED) && gr.getBoolean(Main.DEHYDRATED))
        {
            playerEntity.sendMessage(Text.of("Overhydrated and Dehydrated Gamemodes dont mix... WHY DID YOU DO THIS? Disabling Both for you <3"), false);
            playerEntity.getServer().getGameRules().get(Main.OVERHYDRATED).set(false, playerEntity.getServer());
            playerEntity.getServer().getGameRules().get(Main.DEHYDRATED).set(false, playerEntity.getServer());
        }else
        {
            if(gr.getBoolean(Main.DEHYDRATED))
            {
                if(Main.isInWater(block))
                {
                    Main.killPlayerIfNotCreative(playerEntity);
                }
            }
            if(gr.getBoolean(Main.OVERHYDRATED))
            {
                if(!Main.isInWater(block))
                {
                    OUT_OF_WATER_TIME++;
                }
                else
                {
                    OUT_OF_WATER_TIME = 0;
                }
                if(OUT_OF_WATER_TIME == 40)
                {
                    Main.killPlayerIfNotCreative(playerEntity);
                }
                LivingEntity livingEntity = (LivingEntity) playerEntity;
            }
        }
    }
}
