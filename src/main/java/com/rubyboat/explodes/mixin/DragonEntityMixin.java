package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(EnderDragonEntity.class)
public class DragonEntityMixin {
    @Shadow public int ticksSinceDeath;

    @Inject(at = @At("HEAD"), method = "destroyBlocks", cancellable = true)
    public void destroyBlocks(Box box, CallbackInfoReturnable<Boolean> cir)
    {
        EnderDragonEntity enderDragonEntity = (EnderDragonEntity)(Object) this;
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.floor(box.minY);
        int k = MathHelper.floor(box.minZ);
        int l = MathHelper.floor(box.maxX);
        int m = MathHelper.floor(box.maxY);
        int n = MathHelper.floor(box.maxZ);
        boolean bl = false;
        boolean bl2 = false;

        for(int o = i; o <= l; ++o) {
            for(int p = j; p <= m; ++p) {
                for(int q = k; q <= n; ++q) {
                    BlockPos blockPos = new BlockPos(o, p, q);
                    BlockState blockState = enderDragonEntity.world.getBlockState(blockPos);
                    if (!blockState.isAir() && blockState.getMaterial() != Material.FIRE) {
                        bl2 = enderDragonEntity.world.removeBlock(blockPos, false) || bl2;
                        FallingBlockEntity fallingBlock = new FallingBlockEntity(enderDragonEntity.world, enderDragonEntity.prevX, enderDragonEntity.prevY, enderDragonEntity.prevZ, enderDragonEntity.world.getBlockState(blockPos));
                        fallingBlock.setPos(enderDragonEntity.getX(), enderDragonEntity.getY(), enderDragonEntity.getZ());
                        enderDragonEntity.world.spawnEntity(fallingBlock);
                    }
                }
            }
        }

        if (bl2) {
            BlockPos blockPos2 = new BlockPos(i + enderDragonEntity.getRandom().nextInt(l - i + 1), j + enderDragonEntity.getRandom().nextInt(m - j + 1), k + enderDragonEntity.getRandom().nextInt(n - k + 1));
            enderDragonEntity.world.syncWorldEvent(2008, blockPos2, 0);
        }

        cir.setReturnValue(bl2);
        cir.cancel();
    }

    @Inject(at = @At("HEAD"), method = "updatePostDeath", cancellable = true)
    public void updatePostDeath(CallbackInfo ci)
    {
        EnderDragonEntity enderDragonEntity = (EnderDragonEntity)(Object) this;
        GameRules gr = enderDragonEntity.getEntityWorld().getGameRules();
        if(gr.getBoolean(Main.IS_TNT_GAMEMODE))
        {
            World world = enderDragonEntity.getEntityWorld();
            Main.spawnTNT(enderDragonEntity);
            TntEntity tntEntity = new TntEntity(world, enderDragonEntity.getX(), enderDragonEntity.getY(), enderDragonEntity.getZ(), null);
            tntEntity.setFuse(world.getGameRules().getInt(Main.FUSE_TICKS));
            tntEntity.setPos(enderDragonEntity.prevX, enderDragonEntity.prevY, enderDragonEntity.prevZ);
            world.spawnEntity(tntEntity);
            world.createExplosion(enderDragonEntity, enderDragonEntity.prevX, enderDragonEntity.prevY, enderDragonEntity.prevZ, 10, Explosion.DestructionType.BREAK);
        }
        if(gr.getBoolean(Main.Unfair))
        {
            if(this.ticksSinceDeath == 20)
            {
                EnderDragonEntity newdragon = new EnderDragonEntity(EntityType.ENDER_DRAGON, enderDragonEntity.getEntityWorld());
                newdragon.setPos(enderDragonEntity.prevX, enderDragonEntity.prevY + 10, enderDragonEntity.prevZ);
                newdragon.setCustomName(Text.of("ENDERDRAGON BUT BETTER"));
                enderDragonEntity.getEntityWorld().spawnEntity(newdragon);
            }
        }
    }
}
