package com.rubyboat.explodes.mixin;

import com.rubyboat.explodes.Main;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    public void use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> ci)
    {
        if(world.getGameRules().getBoolean(Main.Unfair))
        {
            if (!world.isClient) {
                player.sendMessage(Text.of("You are allergic to Wheat"), false);
                Main.killPlayerIfNotCreative(player);
            }
        }
    }
}
